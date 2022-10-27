package com.rit.shared.helper

import android.annotation.SuppressLint
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.UserNotAuthenticatedException
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.rit.shared.crypto.CryptographyManager
import java.security.InvalidKeyException
import javax.crypto.Cipher
import timber.log.Timber

class BiometricsHelper constructor(private val fragment: Fragment) {

    private var biometricPrompt: BiometricPrompt
    private var promptInfo: BiometricPrompt.PromptInfo
    private var cryptographyManager: CryptographyManager = CryptographyManager.getInstance()
    private var secretKeyName: String = "RWS_Guest"

    private var onAuthenticated: ((Cipher) -> Unit)? = null

    private var onClickedNegativeButton: (() -> Unit)? = null

    init {
        biometricPrompt = createBiometricPrompt()
        promptInfo = createPromptInfo()
    }

    private fun createBiometricPrompt(): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(fragment.requireContext())

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            @SuppressLint("RestrictedApi")
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Timber.d("$errorCode :: $errString")
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    onClickedNegativeButton?.invoke()
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Timber.d("Authentication failed for an unknown reason")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Timber.d("Authentication was successful")
                result.cryptoObject?.cipher?.let {
                    onAuthenticated?.invoke(it)
                }
            }
        }

        // The API requires the client/Activity context for displaying the prompt view
        return BiometricPrompt(fragment, executor, callback)
    }

    private fun createPromptInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("Set up biometric")
            .setSubtitle("Confirm biometric to continue")
            .setDescription(
                "Use biometric to skip entering your password. You can turn " +
                    "this off anytime via settings in your account."
            )
            .setConfirmationRequired(true)
            .setNegativeButtonText("Cancel")
            // .setDeviceCredentialAllowed(true) // Allow PIN/pattern/password authentication.
            // Also note that setDeviceCredentialAllowed and setNegativeButtonText are
            // incompatible so that if you uncomment one you must comment out the other
            .build()
    }

    fun authenticateToEncrypt(
        onClickedNegativeButton: () -> Unit,
        onError: () -> Unit,
        onAuthenticated: (Cipher) -> Unit
    ) {
        this.onClickedNegativeButton = onClickedNegativeButton
        this.onAuthenticated = onAuthenticated
        if (BiometricManager.from(fragment.requireContext().applicationContext)
            .canAuthenticate() == BiometricManager
                .BIOMETRIC_SUCCESS
        ) {
            try {
                val cipher = cryptographyManager.getInitializedCipherForEncryption(secretKeyName)
                biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
            } catch (e: InvalidKeyException) {
                Timber.e("Key is invalid.")
            } catch (e: UserNotAuthenticatedException) {
                Timber.d("The key's validity timed out.")
                authenticateToEncrypt(onClickedNegativeButton, onError, onAuthenticated)
            } catch (e: KeyPermanentlyInvalidatedException) {
                Timber.e(e)
                onError()
            } catch (e: Exception) {
                Timber.e(e)
                onError()
            }
        }
    }

    fun authenticateToDecrypt(
        iv: ByteArray,
        onClickedNegativeButton: () -> Unit,
        onError: () -> Unit,
        onAuthenticated: (Cipher) -> Unit
    ) {
        this.onClickedNegativeButton = onClickedNegativeButton
        this.onAuthenticated = onAuthenticated
        if (BiometricManager.from(fragment.requireContext().applicationContext)
            .canAuthenticate() == BiometricManager
                .BIOMETRIC_SUCCESS
        ) {
            try {
                val cipher = cryptographyManager.getInitializedCipherForDecryption(
                    secretKeyName, iv
                )
                biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
            } catch (e: InvalidKeyException) {
                Timber.e("Key is invalid.")
            } catch (e: UserNotAuthenticatedException) {
                Timber.d("The key's validity timed out.")
                authenticateToDecrypt(iv, onClickedNegativeButton, onError, onAuthenticated)
            } catch (e: KeyPermanentlyInvalidatedException) {
                Timber.e(e)
                onError()
            } catch (e: Exception) {
                Timber.e(e)
                onError()
            }
        }
    }

    fun hasBiometric(): Boolean {
        val biometricManager = BiometricManager.from(fragment.requireContext())
        return when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Timber.d("App can authenticate using biometrics.")
                true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Timber.e("No biometric features available on this device.")
                false
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Timber.e("Biometric features are currently unavailable.")
                false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Timber.e(
                    "The user hasn't associated any biometric credentials with their account."
                )
                false
            }
            else -> false
        }
    }
}

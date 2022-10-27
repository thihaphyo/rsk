package com.rit.shared.generator

import android.util.Base64
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.interfaces.RSAPublicKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.X509EncodedKeySpec

class PublicKeyReader {
    companion object {
        @JvmStatic
        fun get(publicKey: String? = null): RSAPublicKey? {
            var publicKeyContent = publicKey
            try {
                publicKeyContent = publicKeyContent?.replace("\\n".toRegex(), "")
                    ?.replace("-----BEGIN PUBLIC KEY-----", "")
                    ?.replace("-----END PUBLIC KEY-----", "")
                val kf = KeyFactory.getInstance("RSA")
                val keySpecX509 =
                    X509EncodedKeySpec(Base64.decode(publicKeyContent, Base64.NO_WRAP))
                return kf.generatePublic(keySpecX509) as RSAPublicKey
            } catch (ex: NoSuchAlgorithmException) {
                ex.printStackTrace()
            } catch (ex: InvalidKeySpecException) {
                ex.printStackTrace()
            }
            return null
        }
    }
}

package com.rit.shared.generator

import com.nimbusds.jose.EncryptionMethod
import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JWEAlgorithm
import com.nimbusds.jose.JWEHeader
import com.nimbusds.jose.JWEObject
import com.nimbusds.jose.Payload
import com.nimbusds.jose.crypto.RSAEncrypter
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException

class EncryptGenerator {
    companion object {
        private const val algorithm = "RSA-OAEP-256"
        private const val encMethod = "A128CBC-HS256"

        @JvmStatic
        fun generateEncrypt(
            plainText: String? = null,
            publicKey: String? = null
        ): String? {
            try {
                val header = JWEHeader(
                    JWEAlgorithm.parse(algorithm),
                    EncryptionMethod.parse(encMethod)
                )
                val payload = Payload(plainText)
                val jweObject = JWEObject(header, payload)
                val publicKeyReader = PublicKeyReader.get(publicKey)
                val encrypted = RSAEncrypter(publicKeyReader)
                jweObject.encrypt(encrypted)
                return jweObject.serialize()
            } catch (ex: NoSuchAlgorithmException) {
                ex.printStackTrace()
            } catch (ex: InvalidKeySpecException) {
                ex.printStackTrace()
            } catch (ex: JOSEException) {
                ex.printStackTrace()
            }
            return null
        }
    }
}

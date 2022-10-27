package com.rit.shared.crypto

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by khunzohn on 7/26/20 at codigo
 */
fun String.encrypt(password: String, iv: String): ByteArray {

    val keySpec = SecretKeySpec(password.toByteArray(Charsets.UTF_8), "AES")

    val ivSpec = IvParameterSpec(iv.toByteArray(Charsets.UTF_8))
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
    return cipher.doFinal(this.toByteArray(Charsets.UTF_8))
}

fun ByteArray.decrypt(
    password: String,
    iv: String
): ByteArray {
    val keySpec = SecretKeySpec(password.toByteArray(Charsets.UTF_8), "AES")

    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    val ivSpec = IvParameterSpec(iv.toByteArray(Charsets.UTF_8))
    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
    return cipher.doFinal(this)
}

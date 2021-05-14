package com.bradpark.crypto

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

private const val KEY = "fac5cb0eda8e264ba0a3f60c9c741601"
private const val IV = "9c741601e1bff80e"

object ChCrypto {
    private val ivParameterSpec = IvParameterSpec(IV.toByteArray())
    private val secretKeySpec = try {
        SecretKeySpec(KEY.toByteArray(), "AES")
    } catch (e: Exception) {
        println("Error while generating key: $e")
        null
    }

    /**
     * 암호화 함수
     *
     * @param plainText
     * @return
     */
    fun encrypt(plainText: String): ByteArray? = try {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)
        cipher.doFinal(plainText.toByteArray())
    } catch (e: Exception) {
        println("Error while encrypting: $e")
        null
    }

    /**
     * 복호화 함수
     *
     * @param cipherText
     * @return
     */
    fun decrypt(cipherText: ByteArray?): String? = try {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)
        String(cipher.doFinal(cipherText))
    } catch (e: Exception) {
        println("Error while decrypting: $e")
        null
    }
}
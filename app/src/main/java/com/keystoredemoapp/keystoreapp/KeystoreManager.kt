package com.keystoredemoapp.keystoreapp

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class KeystoreManager {

    private val ANDROID_KEYSTORE_TYPE = "AndroidKeyStore"
    private val SECRET_KEY_ALIAS = "secret"

    var encryptedBytes: ByteArray? = null

    private val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE_TYPE).apply {
        load(null)
    }

    private val secretKey = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES).apply {
        init(
            KeyGenParameterSpec.Builder(
                SECRET_KEY_ALIAS,
                // purposes for using the key
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setUserAuthenticationRequired(false)
                .setRandomizedEncryptionRequired(true)
                .build()
        )
    }.generateKey()

    private fun getKey(): SecretKey {
        val existingKey = keyStore.getEntry(SECRET_KEY_ALIAS, null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: secretKey
    }

    private val cipher = Cipher.getInstance(
        "${KeyProperties.KEY_ALGORITHM_AES}/" +
                "${KeyProperties.BLOCK_MODE_CBC}/" +
                KeyProperties.ENCRYPTION_PADDING_NONE
    )

    private val getEncryptCipher get() = cipher.apply {
        init(Cipher.ENCRYPT_MODE, getKey())
    }

    fun getDecryptCipher() = cipher.apply {
        init(
            Cipher.DECRYPT_MODE,
            getKey(),
            IvParameterSpec(cipher.iv)
        )
    }!!

    fun encrypt(contentToEncrypt: String): String {

        var temp = contentToEncrypt
        while (temp.toByteArray().size % 16 != 0)
            temp += "\u0020"

        val encryptedBytes = getEncryptCipher.doFinal(temp.toByteArray(Charsets.UTF_8))

        this.encryptedBytes = encryptedBytes

        return encryptedBytes.decodeToString()

    }

    fun decrypt(): String {

        return getDecryptCipher().doFinal(encryptedBytes).decodeToString()

    }

}
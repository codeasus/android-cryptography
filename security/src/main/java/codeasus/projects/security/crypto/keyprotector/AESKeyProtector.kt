package codeasus.projects.security.crypto.keyprotector

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object AESKeyProtector: KeyProtector {
    private const val KEYSTORE_ALIAS_AES = "EnigmaApp_AESCryptoAlias"
    private const val PROVIDER = "AndroidKeyStore"
    private const val ENCRYPTION_MODE_AES_GCM_NO_PADDING = "AES/GCM/NoPadding"

    private const val STRING_ERROR_SECRET_KEY = "Encryption/Decryption SecretKey has not been generated"

    private val TAG = "DBG@CRYPTO@${AESKeyProtector::class.java.name}"

    override fun init() {
        generateSecretKey()
    }

    private fun generateSecretKey() {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, PROVIDER)
        val keyGenParameterSpec = KeyGenParameterSpec
            .Builder(KEYSTORE_ALIAS_AES, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setRandomizedEncryptionRequired(false)
            .build()

        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    private fun getSecretKey(): SecretKey? {
        val keyStore = KeyStore.getInstance(PROVIDER).apply {
            load(null)
        }
        if (keyStore.containsAlias(KEYSTORE_ALIAS_AES)) {
            val secretKeyEntry = keyStore.getEntry(KEYSTORE_ALIAS_AES, null) as KeyStore.SecretKeyEntry
            return secretKeyEntry.secretKey
        }
        throw RuntimeException(STRING_ERROR_SECRET_KEY)
    }

    fun isKeyGenerated(): Boolean {
        val keyStore = KeyStore.getInstance(PROVIDER).apply {
            load(null)
        }
        return keyStore.containsAlias(KEYSTORE_ALIAS_AES)
    }

    override fun encrypt(data: ByteArray, ivBytes: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(ENCRYPTION_MODE_AES_GCM_NO_PADDING)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(), GCMParameterSpec(128, ivBytes))
        return cipher.doFinal(data)
    }

    override fun encrypt(data: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(ENCRYPTION_MODE_AES_GCM_NO_PADDING)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        return cipher.doFinal(data)
    }

    override fun decrypt(cipherData: ByteArray, ivBytes: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(ENCRYPTION_MODE_AES_GCM_NO_PADDING)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), GCMParameterSpec(128, ivBytes))
        return cipher.doFinal(cipherData)
    }

    override fun decrypt(cipherData: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(ENCRYPTION_MODE_AES_GCM_NO_PADDING)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey())
        return cipher.doFinal(cipherData)
    }
}
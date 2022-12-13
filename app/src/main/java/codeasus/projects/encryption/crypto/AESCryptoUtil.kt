package codeasus.projects.encryption.crypto

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AESCryptoUtil {
    private const val ALGORITHM_TYPE = "AES"
    private const val ENCRYPTION_MODE_AES_CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding"
    private const val KEY_SIZE_AES = 256

    private val TAG = AESCryptoUtil::class.java.name

    @Throws(NoSuchAlgorithmException::class)
    fun generateSecretKey(): String {
        val secureRandom = SecureRandom()
        val keyGenerator: KeyGenerator = KeyGenerator.getInstance(ALGORITHM_TYPE)
        keyGenerator.init(KEY_SIZE_AES, secureRandom)
        val key = keyGenerator.generateKey()
        return Base64.encodeToString(key.encoded, Base64.DEFAULT)
    }

    fun generateInitializationVector(): String {
        val initializationVector = ByteArray(16)
        val secureRandom = SecureRandom()
        secureRandom.nextBytes(initializationVector)
        return Base64.encodeToString(initializationVector, Base64.DEFAULT)
    }

    fun encrypt(data: String, sk: String, iv: String): ByteArray {
        val secretKey = base64ToSecretKey(sk)
        val ivByteArray = Base64.decode(iv, Base64.DEFAULT)
        val cipher: Cipher = Cipher.getInstance(ENCRYPTION_MODE_AES_CBC_PKCS5_PADDING)
        val ivParameterSpec = IvParameterSpec(ivByteArray)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)
        return cipher.doFinal(data.toByteArray(StandardCharsets.UTF_8))
    }

    fun decrypt(cipherData: ByteArray, sk: ByteArray, iv: String): String {
        val secretKey = byteArrayToSecretKey(sk)
        val ivByteArray = Base64.decode(iv, Base64.DEFAULT)
        val cipher: Cipher = Cipher.getInstance(ENCRYPTION_MODE_AES_CBC_PKCS5_PADDING)
        val ivParameterSpec = IvParameterSpec(ivByteArray)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec)
        return String(cipher.doFinal(cipherData), StandardCharsets.UTF_8)
    }

    private fun byteArrayToSecretKey(secretKey: ByteArray): SecretKey {
        val input = Base64.decode(secretKey, Base64.DEFAULT)
        val secretKeySpec = SecretKeySpec(input, 0, input.size, ALGORITHM_TYPE);
        return secretKeySpec
    }

    private fun base64ToSecretKey(secretKey: String): SecretKey {
        val input = Base64.decode(secretKey, Base64.DEFAULT)
        val secretKeySpec = SecretKeySpec(input, 0, input.size, ALGORITHM_TYPE);
        return secretKeySpec
    }
}
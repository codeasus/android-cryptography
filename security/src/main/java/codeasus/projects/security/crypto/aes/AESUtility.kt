package codeasus.projects.security.crypto.aes

import codeasus.projects.security.crypto.util.dataToB64StrKey
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

object AESUtility {
    private const val ALGORITHM_TYPE = "AES"
    private const val ENCRYPTION_MODE = "AES/CBC/PKCS5Padding"
    private const val KEY_SIZE_AES = 256
    private const val IV_BYTE_ARRAY_LENGTH = 16

    private const val TAG = "DBG@CRYPTO@AES"

    // b64, B64 -> base64, Base64
    // data -> ByteArray

    fun generateSecretKey(): SecretKey {
        val kG = KeyGenerator.getInstance(ALGORITHM_TYPE)
        kG.init(KEY_SIZE_AES, SecureRandom())
        return kG.generateKey()
    }

    fun generateIV(): IvParameterSpec {
        val iv = ByteArray(IV_BYTE_ARRAY_LENGTH)
        SecureRandom().nextBytes(iv)
        return IvParameterSpec(iv)
    }

    fun encrypt(data: ByteArray, sekKey: SecretKey, iv: IvParameterSpec): ByteArray {
        val cipher = Cipher.getInstance(ENCRYPTION_MODE)
        cipher.init(Cipher.ENCRYPT_MODE, sekKey, iv)
        return cipher.doFinal(data)
    }

    fun decrypt(cipherData: ByteArray, secKey: SecretKey, iv: IvParameterSpec): ByteArray {
        val cipher = Cipher.getInstance(ENCRYPTION_MODE)
        cipher.init(Cipher.DECRYPT_MODE, secKey, iv)
        return cipher.doFinal(cipherData)
    }

    fun dataToIV(dataIV: ByteArray): IvParameterSpec {
        return IvParameterSpec(dataIV)
    }

    fun ivToB64Str(iv: IvParameterSpec): String {
        return dataToB64StrKey(iv.iv)
    }
}
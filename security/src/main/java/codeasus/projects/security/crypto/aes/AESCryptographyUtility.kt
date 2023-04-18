package codeasus.projects.security.crypto.aes

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AESCryptographyUtility {
    private const val ALGORITHM_TYPE = "AES"
    private const val ENCRYPTION_MODE = "AES/CBC/PKCS5Padding"
    private const val KEY_SIZE_AES = 256
    private const val IV_BYTE_ARRAY_LENGTH = 16

    private val TAG = "DBG@CRYPTO@${AESCryptographyUtility::class.java}"

    // b64, B64 -> base64, Base64

    fun generateSK(): SecretKey {
        val kG = KeyGenerator.getInstance(ALGORITHM_TYPE)
        kG.init(KEY_SIZE_AES, SecureRandom())
        return kG.generateKey()
    }

    fun generateIV(): IvParameterSpec {
        val iV = ByteArray(IV_BYTE_ARRAY_LENGTH)
        SecureRandom().nextBytes(iV)
        return IvParameterSpec(iV)
    }

    fun encrypt(data: ByteArray, sk: SecretKey, iv: IvParameterSpec): ByteArray {
        val cipher = Cipher.getInstance(ENCRYPTION_MODE)
        cipher.init(Cipher.ENCRYPT_MODE, sk, iv)
        return cipher.doFinal(data)
    }

    fun decrypt(data: ByteArray, sK: SecretKey, iV: IvParameterSpec): ByteArray {
        val cipher = Cipher.getInstance(ENCRYPTION_MODE)
        cipher.init(Cipher.DECRYPT_MODE, sK, iV)
        return cipher.doFinal(data)
    }

    fun b64EncodedStrToSK(b64EncodedSK: String): SecretKey {
        val decodedByteArraySK = Base64.decode(b64EncodedSK, Base64.NO_WRAP)
        return SecretKeySpec(decodedByteArraySK, 0, decodedByteArraySK.size, ALGORITHM_TYPE)
    }

    fun b64EncodedStrToIV(b64EncodedIV: String): IvParameterSpec {
        val decodedByteArrayIV = Base64.decode(b64EncodedIV, Base64.NO_WRAP)
        return IvParameterSpec(decodedByteArrayIV)
    }

    fun b64EncodedByteArrayToSK(b64EncodedSK: ByteArray): SecretKey {
        return SecretKeySpec(b64EncodedSK, 0, b64EncodedSK.size, ALGORITHM_TYPE)
    }

    fun b64EncodedByteArrayToIV(b64EncodedIV: ByteArray): IvParameterSpec {
        return IvParameterSpec(b64EncodedIV)
    }

    // Convert SecretKey to Base64 String for persistence & communication
    fun sKToB64EncodedStr(sK: SecretKey): String {
        return Base64.encodeToString(sK.encoded, Base64.NO_WRAP)
    }

    // Convert Initialization Vector to Base64 String for persistence & communication
    fun ivToB64EncodedStr(iV: IvParameterSpec): String {
        return Base64.encodeToString(iV.iv, Base64.NO_WRAP)
    }
}
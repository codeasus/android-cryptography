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
    private const val IV_BYTE_ARRAY_LENGTH = 16

    private const val TAG = "DBG@CRYPTO -> AESCryptoUtil"

    // b64, B64 -> base64, Base64

    fun getSKAndIVPair(): Pair<SecretKey, IvParameterSpec> {
        return Pair(generateSK(), generateIV())
    }

    private fun generateSK(): SecretKey {
        val kG = KeyGenerator.getInstance(ALGORITHM_TYPE)
        kG.init(KEY_SIZE_AES, SecureRandom())
        return kG.generateKey()
    }

    private fun generateIV(): IvParameterSpec {
        val iV = ByteArray(IV_BYTE_ARRAY_LENGTH)
        SecureRandom().nextBytes(iV)
        return IvParameterSpec(iV)
    }

    fun encrypt(data: ByteArray, sk: SecretKey, iv: IvParameterSpec): ByteArray {
        val cipher = Cipher.getInstance(ENCRYPTION_MODE_AES_CBC_PKCS5_PADDING)
        val b64EncodedSk = Base64.encodeToString(sk.encoded, Base64.NO_WRAP)
        val b64EncodedIV = Base64.encodeToString(iv.iv, Base64.NO_WRAP)
        Timber.d("$TAG: SECRET_KEY: $b64EncodedSk; IV: $b64EncodedIV;")
        cipher.init(Cipher.ENCRYPT_MODE, sk, iv)
        return cipher.doFinal(data)
    }

    fun decrypt(data: ByteArray, sK: SecretKey, iV: IvParameterSpec): ByteArray {
        val cipher = Cipher.getInstance(ENCRYPTION_MODE_AES_CBC_PKCS5_PADDING)
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

    fun b64EncodedStrSKToByteArray(b64EncodedSK: String): ByteArray {
        return Base64.decode(b64EncodedSK, Base64.NO_WRAP)
    }

    fun b64EncodedStrIVToByteArray(b64EncodedIV: String): ByteArray {
        return Base64.decode(b64EncodedIV, Base64.NO_WRAP)
    }

    fun utf8StrToByteArray(utf8Data: String): ByteArray {
        return utf8Data.toByteArray(StandardCharsets.UTF_8)
    }

    fun byteArrayToUTF8Str(byteArrayData: ByteArray): String {
        return String(byteArrayData, StandardCharsets.UTF_8)
    }
}
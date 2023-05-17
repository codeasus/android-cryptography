package codeasus.projects.security.crypto.util

import android.util.Base64
import java.nio.charset.StandardCharsets
import javax.crypto.spec.SecretKeySpec

object CryptoUtil {
    private const val FLAG_B64 = Base64.NO_WRAP
    private const val ALGORITHM_TYPE = "AES"

    // b64, B64 -> base64, Base64
    // data -> ByteArray

    fun uTF8StrToData(utf8Str: String): ByteArray {
        return utf8Str.toByteArray(StandardCharsets.UTF_8)
    }

    fun dataToUTF8Str(data: ByteArray): String {
        return String(data, StandardCharsets.UTF_8)
    }

    fun b64StrKeyToData(str: String): ByteArray {
        return Base64.decode(str, FLAG_B64)
    }

    fun dataToB64StrKey(data: ByteArray): String {
        return Base64.encodeToString(data, FLAG_B64)
    }

    fun dataToAESSecretKey(dataSecKey: ByteArray): SecretKeySpec {
        return SecretKeySpec(dataSecKey, 0, dataSecKey.size, ALGORITHM_TYPE)
    }
}
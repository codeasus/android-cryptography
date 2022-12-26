package codeasus.projects.encryption.hash

import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class Hashing {

    inner class MD5Hash {

        @kotlin.jvm.Throws(NoSuchAlgorithmException::class)
        fun generateHash(data: String) {
            val mD = MessageDigest.getInstance("MD5")
            val digestRes = mD.digest(data.toByteArray(StandardCharsets.UTF_8))

        }
    }
}
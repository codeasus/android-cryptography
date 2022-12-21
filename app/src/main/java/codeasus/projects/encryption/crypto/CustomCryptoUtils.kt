package codeasus.projects.encryption.crypto

import java.nio.charset.StandardCharsets

object CustomCryptoUtils {

    private const val BLOCK_LENGTH: Int = 16
    private const val PADDING_ELEMENT: Byte = 0

    fun String.toUTF8ByteArray(): ByteArray {
        return this.toByteArray(StandardCharsets.UTF_8)
    }

    fun String.toBlockPaddedUTF8ByteArray(): ByteArray {
        val inputArray = this.toUTF8ByteArray()
        val inputArraySize = inputArray.size
        return if (inputArraySize % BLOCK_LENGTH == 0) {
            inputArray
        } else {
            val maxBlockSize = (inputArraySize / BLOCK_LENGTH) + 1
            val resultArray = inputArray.copyOf(maxBlockSize * BLOCK_LENGTH)
            resultArray.fill(PADDING_ELEMENT, inputArraySize, resultArray.size)
            resultArray
        }
    }
}
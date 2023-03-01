package codeasus.projects.encryption.hash

import android.os.Build
import androidx.annotation.RequiresApi

object CONSTANTS {
    const val INT_BINARY_SIZE = 32
}

class NegativeNumberException : Exception()

@RequiresApi(Build.VERSION_CODES.O)
fun main() {
//     1   as 8bit :  0000 0001
//     1   as 32bit:  0000 0000 0000 0000 0000 0000 0000 0001
//     0xF as 32bit:  0000 0000 0000 0000 0000 0000 0000 1111
//
//     0000 0000 0000 0000 0000 0000 0000 0000
//     0000 0000 0000 0000 0000 0000 0000 0001
//     3: 11  shr 1 -> 1
//     3: 101 shl 1 -> 110

//    print((0b1 shr 4).and(0xF))
}

private fun Int.positiveIntToBinary(): String {
    if (this < 0) throw NegativeNumberException()
    val res = CharArray(CONSTANTS.INT_BINARY_SIZE) { '0' }
    var num = this
    var counter = 0
    while (num != 0) {
        res[(res.size - 1) - counter++] = (num % 2).digitToChar()
        num /= 2
    }
    return String(res)
}
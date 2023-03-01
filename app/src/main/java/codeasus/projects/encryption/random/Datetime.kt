package codeasus.projects.encryption.random

import android.os.Build
import androidx.annotation.RequiresApi
import java.sql.Timestamp
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
fun main() {

//    Timestamp:  4676404460331997953
//    Datetime: 2022-12-09T15:21:49
//
//    Timestamp:  4676404460629515110
//    Datetime: 2022-12-09T15:24:56
//
//    Timestamp:  4676404456744364049
//    Datetime: 2022-12-09T14:44:13
//
//    Timestamp:  4676404375042756909
//    Datetime: 2022-12-09T00:28:12
//
//    Timestamp:  4676404375052355366
//    Datetime: 2022-12-09T00:28:18

    val uAT = 4_676_404_460_331_997_953
    val uBT = 4_676_404_460_629_515_110
    val uCT = 4_676_404_456_744_364_049
    val uDT = 4_676_404_375_042_756_909
    val uET = 4_676_404_375_052_355_366
    val unknownTimestamps = arrayOf(uAT, uBT, uCT, uDT, uET)

    val a = LocalDateTime.of(2022, 12, 9, 15, 21, 49)
    val b = LocalDateTime.of(2022, 12, 9, 15, 24, 56)
    val c = LocalDateTime.of(2022, 12, 9, 14, 44, 13)
    val d = LocalDateTime.of(2022, 12, 9, 0, 28, 12)
    val e = LocalDateTime.of(2022, 12, 9, 0, 28, 18)
    val fullDateTimes = arrayOf(a, b, c, d, e)

    val aT = Timestamp.UTC(2022, 12, 9, 15, 21, 49)
    val bT = Timestamp.UTC(2022, 12, 9, 15, 24, 56)
    val cT = Timestamp.UTC(2022, 12, 9, 14, 44, 13)
    val dT = Timestamp.UTC(2022, 12, 9, 0, 28, 12)
    val eT = Timestamp.UTC(2022, 12, 9, 0, 28, 18)
    val timestamps = arrayOf(aT, bT, cT, dT, eT)

    timestamps.forEachIndexed { index, timestamp ->
        println("${unknownTimestamps[index]} -> $timestamp: diff: ${unknownTimestamps[index] - timestamp}")
    }
}
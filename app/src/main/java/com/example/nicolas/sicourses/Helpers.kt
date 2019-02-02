package Helpers

import java.math.RoundingMode
import java.text.DecimalFormat

fun convertIntVecToSting(intVec: ArrayList<Int>): String {
    var s = ""
    for (i in 0..intVec.size-1) {
        if (intVec[i] > 0) {
            for (j in 1..intVec[i]) {
                s += "${10-i},"
            }
        }
    }
    return s.removeSuffix(",")
}

fun convertStringVecIntoMedia(string: String): Double {
    return string.split(",").map { it.toInt() }.average()
}


fun roundOnDecimal(double: Double): String {
    val df = DecimalFormat("#.###")
    df.roundingMode = RoundingMode.CEILING
    return df.format(double)
}

fun roundOnInteger(double: Double): String {
    val df = DecimalFormat("#")
    df.roundingMode = RoundingMode.CEILING
    return df.format(double)
}
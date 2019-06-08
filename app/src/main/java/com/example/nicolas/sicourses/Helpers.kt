package Helpers

import com.example.nicolas.sicourses.CourseDataClass
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

fun convertCourseToString(c: CourseDataClass): String {
    return  c.nombre + "/;" +
            c.empresa + "/;" +
            c.lugar + "/;" +
            c.de + "/;" +
            c.hasta + "/;" +
            c.evals + "/;" +
            c.media.toString() + "/;" +
            c.numero.toString()
}

fun convertStringToCourse(s: String): CourseDataClass {
    val splitted = s.split("/;")
    return CourseDataClass(
            nombre = splitted[0],
            empresa = splitted[1],
            lugar = splitted[2],
            de = splitted[3],
            hasta = splitted[4],
            evals = splitted[5],
            media = splitted[6].toDouble(),
            numero = splitted[7].toInt())
}




//fun main(args: Array<String>) {
//
//    val c1 = cl("Madrid", "emp1", "tia")
//    val c2 = cl("Barca", "emp2", "tia")
//    val c3 = cl("ZARAGO", "EMP3", "tia2")
//
//    val l = mutableListOf<cl>()
//    val ls = mutableListOf<cl>()
//    l.add(c1)
//    l.add(c2)
//    l.add(c3)
//    var nameMap: MutableList<String> = l.map { ctos(it) }.toMutableList();
//    val s = nameMap.joinToString("/n")
//    println(s)
//    val courses = s.split("/n").toMutableList();
//    println(courses)
//    courses.forEach {println(it)}
//    val myc1 = courses[1]
//    println(myc1)
//    println(stoc(myc1))
//    var newCourses: MutableList<cl> = courses.map { stoc(it) }.toMutableList();
//    println(newCourses)
//}}
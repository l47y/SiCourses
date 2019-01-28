package com.example.nicolas.sicourses

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat

@Parcelize
data class CourseDataClass(val nombre: String,
                           val lugar: String,
                           val empresa: String,
                           val evals: String,
                           val de: String,
                           val hasta: String,
                           val media: Double,
                           val numero: Int): Parcelable


// Interface for sorting
class CompareCourses {

    companion object : Comparator<CourseDataClass> {

        val format = SimpleDateFormat("dd.MM.yyyy")

        override fun compare(a: CourseDataClass, b: CourseDataClass): Int = when {
            format.parse(a.hasta) > format.parse(b.hasta) -> 1
            format.parse(a.hasta) < format.parse(b.hasta) -> -1
            else -> 0
        }
    }
}
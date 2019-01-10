package com.example.nicolas.sicourses

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CourseDataClass(val nombre: String,
                           val lugar: String,
                           val empresa: String,
                           val evals: String,
                           val media: Double): Parcelable
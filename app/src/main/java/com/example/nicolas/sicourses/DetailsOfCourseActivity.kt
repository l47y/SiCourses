package com.example.nicolas.sicourses

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_details_of_course.*

class DetailsOfCourseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_of_course)

        val intent = intent
        detailsNombre.text = intent.getStringExtra("nombre")
        detailsEmpresa.text = intent.getStringExtra("empresa")
        detailsLugar.text = intent.getStringExtra("lugar")
        detailsEvaluaciones.text = intent.getStringExtra("evaluaciones")
        detailsMedia.text = intent.getStringExtra("media").toString()
        val de = intent.getStringExtra("de")
        val hasta = intent.getStringExtra("hasta")
        detailsFecha.text = "$de - $hasta"
        val evalsInt= intent.getStringExtra("evaluaciones").split(",").dropLast(1)
        detailsParticipants.text = evalsInt.size.toString()
        detailsEvaluaciones.text = intent.getStringExtra("evaluaciones")


    }
}

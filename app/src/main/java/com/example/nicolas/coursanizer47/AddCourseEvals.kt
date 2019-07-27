package com.example.nicolas.coursanizer47

import Helpers.convertIntVecToSting
import Helpers.convertStringVecIntoMedia
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_course_evals.*


class AddCourseEvals : AppCompatActivity() {

    var diez = 0
    var nueve = 0
    var ocho = 0
    var siete = 0
    var seis = 0
    var cinco = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course_evals)

        val intent = getIntent()
        val nombre = intent.getStringExtra("nombre")
        val lugar = intent.getStringExtra("lugar")
        val empresa = intent.getStringExtra("empresa")
        val de = intent.getStringExtra("de")
        val hasta = intent.getStringExtra("hasta")


        // Croller Label updates
        croller_diez.setOnProgressChangedListener {
            diez = croller_diez.progress
            val prog = croller_diez.progress.toString()
            croller_diez.label = "Número 10s: $prog"
        }
        croller_nueve.setOnProgressChangedListener {
            nueve = croller_nueve.progress
            val prog = croller_nueve.progress.toString()
            croller_nueve.label = "Número 9s: $prog"
        }
        croller_ocho.setOnProgressChangedListener {
            ocho = croller_ocho.progress
            val prog = croller_ocho.progress.toString()
            croller_ocho.label = "Número 8s: $prog"
        }
        croller_siete.setOnProgressChangedListener {
            siete = croller_siete.progress
            val prog = croller_siete.progress.toString()
            croller_siete.label = "Número 7s: $prog"
        }
        croller_seis.setOnProgressChangedListener {
            seis = croller_seis.progress
            val prog = croller_seis.progress.toString()
            croller_seis.label = "Número 6s: $prog"
        }
        croller_cinco.setOnProgressChangedListener {
            cinco = croller_cinco.progress
            val prog = croller_cinco.progress.toString()
            croller_cinco.label = "5s: $prog"
        }

        btn_listo_add_course_evals.setOnClickListener {
            val evalVec = arrayListOf(diez, nueve, ocho, siete, seis, cinco)
            if (evalVec.sum() == 0) {
                Toast.makeText(this, "Tienes que añadir al menos una evaluación!", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                val evalVecParcable = convertIntVecToSting(evalVec)
                val media = convertStringVecIntoMedia(evalVecParcable)
                intent.putExtra("nombre", nombre)
                intent.putExtra("lugar", lugar)
                intent.putExtra("empresa", empresa)
                intent.putExtra("evals", evalVecParcable)
                intent.putExtra("media", media)
                intent.putExtra("de", de)
                intent.putExtra("hasta", hasta)

                intent.putExtra("I_COME_FROM", "AddCourse")
                startActivity(intent)
            }
        }
    }
}

package com.example.nicolas.sicourses

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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

//        val diez: Int = croller_diez.progress.toString().toInt()
//        val nueve: Int = croller_nueve.progress.toString().toInt()
//        val ocho = croller_ocho.progress.toString().toInt()
//        val siete = croller_siete.progress.toString().toInt()
//        val seis = croller_seis.progress.toString().toInt()
//        val cinco = croller_cinco.progress.toString().toInt()

        // Croller Label updates
        croller_diez.setOnProgressChangedListener({
            diez = croller_diez.progress
            val prog = croller_diez.progress.toString()
            croller_diez.label = "10s: $prog"
        })
        croller_nueve.setOnProgressChangedListener({
            nueve = croller_nueve.progress
            val prog = croller_nueve.progress.toString()
            croller_nueve.label = "9s: $prog"
        })
        croller_ocho.setOnProgressChangedListener({
            ocho = croller_ocho.progress
            val prog = croller_ocho.progress.toString()
            croller_ocho.label = "8s: $prog"
        })
        croller_siete.setOnProgressChangedListener({
            siete = croller_siete.progress
            val prog = croller_siete.progress.toString()
            croller_siete.label = "7s: $prog"
        })
        croller_seis.setOnProgressChangedListener({
            seis = croller_seis.progress
            val prog = croller_seis.progress.toString()
            croller_seis.label = "6s: $prog"
        })
        croller_cinco.setOnProgressChangedListener({
            cinco = croller_cinco.progress
            val prog = croller_cinco.progress.toString()
            croller_cinco.label = "5s: $prog"
        })

        btn_listo_add_course_evals.setOnClickListener {
            val evalVec = arrayListOf(diez, nueve, ocho, siete, seis, cinco)
            val intent = Intent(this, MainActivity::class.java)
            val evalVecParcable = convertIntVecToSting(evalVec)
            val media = convertStringVecIntoMedia(evalVecParcable)
            intent.putExtra("nombre", nombre)
            intent.putExtra("lugar", lugar)
            intent.putExtra("empresa", empresa)
            intent.putExtra("evals", evalVecParcable)
            intent.putExtra("media", media)
            intent.putExtra("I_COME_FROM", "AddCourse")
            startActivity(intent)
        }

    }
    
    private fun convertIntVecToSting(intVec: ArrayList<Int>): String {
        val b = StringBuilder()
        for (i in 0..intVec.size-1) {
            if (intVec[i] > 0) {
                for (j in 1..intVec[i]) {
                    val num = 10-i
                    b.append("$num,")
                }
            }
        }
        return b.toString().removeSuffix(",")
    }

    private fun convertStringVecIntoMedia(string: String): Double {
        val numbers = string.split(",").dropLast(1)
        val result = numbers.map { it.toInt() }
        val num = result.average()
        println("FIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII")
        println(num)
        //val df = DecimalFormat("#.###")
        //return df.format(num).toDouble()
        return num
    }

}

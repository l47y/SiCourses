package com.example.nicolas.coursanizer47

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_course_datos.*
import java.text.SimpleDateFormat
import java.util.*


class AddCourseDatos : AppCompatActivity() {

    // List of biggest cities. Source:
    // https://es.wikipedia.org/wiki/Anexo:Municipios_de_Espa%C3%B1a_por_poblaci%C3%B3n
    private val spanishCities: Array<String> = arrayOf(
        "Madrid", "Barcelona", "Valencia", "Zaragoza", "Sevilla", "Málaga", "Murcia", "Palma",
        "Las Palmas de Gran Canaria", "Bilbao", "Alicante", "Córdoba", "Valladolid", "Vigo",
        "Gijón", "Hospitalet de Llobregat", "Vitoria", "A Coruña", "Granada", "Elche", "Oviedo",
        "Terrasa", "Badalona", "Cartagena", "Jerez de la Frontera", "Sabadell", "Móstoles",
        "Santa Cruz de Tenerife", "Pamplona", "Almería", "Alcalá de Henares", "Fuenlabrada",
        "Leganés", "San Sebastián", "Getafe", "Burgos", "Albacete", "Santander", "Castellón de la Plana",
        "San Cristóbal de la Laguna", "Alcorcón", "Logroñö", "Badajoz", "Huelva", "Salamanca",
        "Marbella", "Lérida", "Dos Hermanas", "Tarragona", "Torrejón de Ardoz", "Mataró",
        "Parla", "León", "Algeciras", "Cádiz", "Santa Coloma de Gramanet", "Alcobendas", "Jaén",
        "Orense", "Reus", "Telde", "Baracaldo", "Teruel", "Soria", "Aranda de Duero", "Tres Cantos",
        "Almussafes")

    private val courseList: Array<String> = arrayOf(
        "S7 300/400 nivel 1",
        "S7 300/400 nivel 2",
        "S7 Ethernet y Profinet",
        "TIA Portal Programación 1",
        "TIA Portal Programación 2",
        "TIA Portal Actualización",
        "TIA Portal Profinet",
        "MP SINUMERIK 840D sl",
        "PEM SINUMERIK 840D sl",
        "PCS 7 curso del",
        "SINAMICS S120",
        "Switching / Routing")


    private var deDate = Date()
    private var hastaDate = Date()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course_datos)


        // Set suggestions for nombre autocompletion
        val autoCompleteNombre = findViewById<AutoCompleteTextView>(R.id.add_course_nombreinput)
        val adapterNombre = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, courseList)
        autoCompleteNombre.setAdapter(adapterNombre)


        // Set suggestions for lugar autocompletion
        val autoCompleteLugar = findViewById<AutoCompleteTextView>(R.id.add_course_lugarinput)
        val adapterLugar = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, spanishCities)
        autoCompleteLugar.setAdapter(adapterLugar)


        add_course_buttonlisto.setOnClickListener {

            val nombre = add_course_nombreinput.text.toString()
            val lugar = add_course_lugarinput.text.toString()
            val empresa = add_course_empresainput.text.toString()
            val de = add_course_deinput.text.toString()
            val hasta = add_course_hastainput.text.toString()

            val allInputs: Array<String?> = arrayOf(nombre, lugar, empresa, de, hasta)
            val checkWhereNotCorrect = allInputs.map {it == null || it == ""}
            val numberNotCorrect: Int = checkWhereNotCorrect.count {it == true}

            // if some input empty than show Toast, else send data to MainActivity
            if (numberNotCorrect > 0) {
                val message = "No se debe dejar vacía ninguna casilla"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            } else if (hastaDate < deDate){
                val message = "No puedes finalizar el curso antes de empezarlo"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, AddCourseEvals::class.java)
                intent.putExtra("nombre", nombre)
                intent.putExtra("lugar", lugar)
                intent.putExtra("empresa", empresa)
                intent.putExtra("de", de)
                intent.putExtra("hasta", hasta)
                startActivity(intent)
            }
        }

        val cal = Calendar.getInstance()

        val dateSetListenerDe = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            deDate = cal.time
            val myFormat = "dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            add_course_deinput.setText(sdf.format(cal.time))

        }

        add_course_deinput.setOnClickListener {
            DatePickerDialog(this, dateSetListenerDe,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        val dateSetListenerHasta = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            hastaDate = cal.time
            val myFormat = "dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            add_course_hastainput.setText(sdf.format(cal.time))

        }

        add_course_hastainput.setOnClickListener {
            DatePickerDialog(this, dateSetListenerHasta,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

    }

}

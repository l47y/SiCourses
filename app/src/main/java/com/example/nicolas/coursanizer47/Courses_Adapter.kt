package com.example.nicolas.coursanizer47

import Helpers.roundOnDecimal
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView


class Courses_Adapter(courses: ArrayList<CourseDataClass>):
    RecyclerView.Adapter<Courses_Adapter.CustomViewHolder>(), Filterable {

    // Initialization of Courses and a copy for filtering
    var Courses: ArrayList<CourseDataClass> = courses // for reuse in CoursesFragment
    private var CoursesFull: ArrayList<CourseDataClass>
    init {
        Courses = courses
        CoursesFull = ArrayList(Courses)
    }

    var clicked_index = -1
    var clicked_index_for_unhighlight = 0 //track if clicked twice to unhighlight item
    var clicked_index_before = -1

    // Viewholder class
    inner class CustomViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val layout = v.findViewById<ConstraintLayout>(R.id.const_layout)
        val nombreView = v.findViewById<TextView>(R.id.recyclerlayout_nombre)
        val lugarView = v.findViewById<TextView>(R.id.recyclerlayout_lugar)
        val empresaView = v.findViewById<TextView>(R.id.recyclerlayout_empresa)
        val mediaView = v.findViewById<TextView>(R.id.recyclerlayout_media)
        val fechaView = v.findViewById<TextView>(R.id.recyclerlayout_fecha)
        val participantesView = v.findViewById<TextView>(R.id.recyclerlayout_participantes)
        val evalsView = v.findViewById<TextView>(R.id.recyclerlayout_evals)
       // val chartofsinglecourseView = v.findViewById<FloatingActionButton>(R.id.chartofsinglecourse)

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolder {
        val layoutInflator = LayoutInflater.from(p0?.context)
        val cell = layoutInflator.inflate(R.layout.recyclerlist_layout, p0, false)
        return CustomViewHolder(cell)
    }

    override fun onBindViewHolder(p0: CustomViewHolder, p1: Int) {

        // Logic to highlight a clicked Item
        p0.layout.setOnClickListener {
            clicked_index = p1
            notifyDataSetChanged()
        }
        if(clicked_index == p1){
            when (clicked_index_for_unhighlight) {
                0 -> {
                    p0.layout.setBackgroundColor(Color.parseColor("#33008577"))
                    p0.participantesView.visibility = View.VISIBLE
                    p0.evalsView.visibility = View.VISIBLE
                    clicked_index_for_unhighlight += 1
                }
                1 -> {
                    p0.layout.setBackgroundColor(Color.parseColor("#ffffff"))
                    p0.participantesView.visibility = View.GONE
                    p0.evalsView.visibility = View.GONE
                    clicked_index_for_unhighlight -= 1
                }
            }
        } else {
            p0.layout.setBackgroundColor(Color.parseColor("#ffffff"))
            p0.participantesView.visibility = View.GONE
            p0.evalsView.visibility = View.GONE
          //  p0.chartofsinglecourseView.visibility = View.GONE
        }

        val Course = Courses.get(p1)
        val empresa = Course.empresa
        val lugar = Course.lugar
        val meanCourse = roundOnDecimal(Course.media)
        val de = Course.de
        val hasta = Course.hasta
        val numero = Course.numero.toString()
        val nombre = Course.nombre
        val evals = Course.evals
        val numberParticipantes = evals.split(",").size.toString()

        val valueCounts = evals.split(",").groupingBy { it }.eachCount()
        var evalString = ""
        for ((key, value) in valueCounts) evalString += value.toString() + "x" + key + ", "
        evalString = evalString.removeSuffix(", ")

        p0.nombreView.text = "$nombre"
        p0.empresaView.text = "Empresa: $empresa"
        p0.lugarView.text = "Lugar: $lugar"
        p0.mediaView.text = "$meanCourse"
        p0.fechaView.text = "$de - $hasta"
        p0.participantesView.text = "Participantes: $numberParticipantes"
        p0.evalsView.text = "Evaluaciones: $evalString"
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return Courses.size
    }

    override fun getFilter(): Filter {
        return RecyclerFilter()
    }

    // Filter class
    inner class RecyclerFilter: Filter() {

        override fun performFiltering(constraint_original: CharSequence?): FilterResults {

            val results = FilterResults()
            val filteredList: ArrayList<CourseDataClass> = ArrayList()
            val constraint = constraint_original.toString().toLowerCase()

            if (constraint != null && constraint.length > 0) {
                for (Course in CoursesFull) {
                    val nombrePattern = Course.nombre.toString().toLowerCase()
                    val lugarPattern = Course.lugar.toString().toLowerCase()
                    val empresaPattern = Course.empresa.toString().toLowerCase()

                    if (nombrePattern.contains(constraint) || lugarPattern.contains(constraint) ||
                            empresaPattern.contains(constraint)) {
                        filteredList.add(Course)
                    }
                }
                results.values = filteredList
            } else {
                results.values = CoursesFull
            }

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                Courses.clear()
                //Courses.addAll(CoursesFull)
                Courses.addAll(results?.values as List<CourseDataClass>)
                notifyDataSetChanged()
        }
    }


}


package com.example.nicolas.sicourses


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_courses.*
import kotlinx.android.synthetic.main.fragment_courses.view.*
import java.nio.file.Files.delete
import android.support.v7.widget.RecyclerView
import com.google.gson.Gson
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class CoursesFragment : Fragment() {

    lateinit var myAdapter: Courses_Adapter
    lateinit var searchView: android.support.v7.widget.SearchView
    lateinit var recyclView: RecyclerView

    var courses = ArrayList<CourseDataClass>()
    private var CoursesFull: ArrayList<CourseDataClass>
    init {
        CoursesFull = ArrayList(courses)
    }

    var clicked_course_index = -1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val Inflater = inflater.inflate(R.layout.fragment_courses, container, false)

        setHasOptionsMenu(true)
        setRetainInstance(true)

        recyclView = Inflater.recyclerView_Courses
        recyclView.layoutManager = LinearLayoutManager(context)

        val bundle = getArguments()
        if (bundle != null) {
            courses = bundle.getParcelableArrayList<CourseDataClass>("courses")
            CoursesFull = ArrayList(courses)
            myAdapter = Courses_Adapter(courses)
            recyclView.adapter = myAdapter
        }

        Inflater.button_addcourse.setOnClickListener {
            val intent = Intent(context, AddCourseDatos::class.java)
            startActivity(intent)
        }

        Inflater.button_sharecourse.setOnClickListener {
            clicked_course_index = myAdapter.clicked_index
            if (clicked_course_index != -1) {
                val shareCourse = courses.get(clicked_course_index)
                val nombre = shareCourse.nombre
                val empresa = shareCourse.empresa
                val lugar = shareCourse.lugar
                val de = shareCourse.de
                val hasta = shareCourse.hasta
                val media = shareCourse.media
                val mediaString = "%.3f".format(media)

                val evals = shareCourse.evals
                val sendString = "He dado un curso de *$nombre* en la empresa *$empresa* en *$lugar*.\n" +
                        "Me han dado " +
                        "las siguientes evaluaciones: \n$evals. \nLa media es: $mediaString. \nLo he dado desde " +
                        "$de hasta $hasta."
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, sendString)
                    type = "text/plain"
                }
                startActivity(sendIntent)
            }
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val delCourse = courses.get(position)
                val nameToDelete = delCourse.nombre

                val builder = AlertDialog.Builder(context!!)
                val string = "Estás seguro que quieres borrar el curso $nameToDelete ?"
                builder.setTitle("Confirmación necesaria")

                builder.setMessage(string)
                builder.setPositiveButton("Si") { dialog, which ->
                    println(courses)
                    println(courses.size)
                    println("HAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
                    println("HOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO")

                    var delIndex = 0
                    // Now delete the swiped object in CoursesFull and not in courses, because when you filter the list
                    // it will only save the filtered list without the deleted element
                    CoursesFull.forEachIndexed {ind, el ->
                        if(el.nombre == delCourse.nombre && el.empresa == delCourse.empresa && el.de == delCourse.de) {
                           delIndex = ind
                        }
                    }
                    CoursesFull.removeAt(delIndex)
                    println(position)
                    //courses.removeAt(position)
                    courses = ArrayList(CoursesFull)
                    println(courses.size)
                    println(courses)
                    saveData()
                    myAdapter = Courses_Adapter(courses)
                    recyclView.adapter = myAdapter
                }
                builder.setNegativeButton("No") { dialog, which ->
                    recyclView.adapter = myAdapter
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }).attachToRecyclerView(recyclView)

        return Inflater
    }

    // Stuff for search Tool bar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        var searchItem = menu!!.findItem(R.id.filterSearch)
        searchView = searchItem.actionView as android.support.v7.widget.SearchView
        searchView.setOnQueryTextListener(object : android.support.v7.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                myAdapter.filter.filter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })
    }

    // Sort List
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.sortPorNombre -> {
                courses = ArrayList(courses.sortedWith(compareBy({ it.nombre})))
                myAdapter = Courses_Adapter(courses)
                recyclView.adapter = myAdapter
                return true
            }
            R.id.sortPorLugar -> {
                courses = ArrayList(courses.sortedWith(compareBy({ it.lugar})))
                myAdapter = Courses_Adapter(courses)
                recyclView.adapter = myAdapter
                return true
            }
            R.id.sortPorEmpresa -> {
                courses = ArrayList(courses.sortedWith(compareBy({ it.empresa})))
                myAdapter = Courses_Adapter(courses)
                recyclView.adapter = myAdapter
                return true
            }
            R.id.sortPorMedia -> {
                courses = ArrayList(courses.sortedWith(compareBy({ it.media})).asReversed())
                myAdapter = Courses_Adapter(courses)
                recyclView.adapter = myAdapter
                return true
            }
            R.id.sortPorFecha -> {
                courses = ArrayList(courses.sortedWith(CompareCourses).asReversed())
                myAdapter = Courses_Adapter(courses)
                recyclView.adapter = myAdapter
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun saveData() {
        val sharedPrefsObj = this.activity?.getSharedPreferences("data", Context.MODE_PRIVATE)
        val editor = sharedPrefsObj?.edit()
        val gson = Gson()
        val json = gson.toJson(courses)
        editor?.putString("savedCourses", json)
        editor?.apply()
    }
}

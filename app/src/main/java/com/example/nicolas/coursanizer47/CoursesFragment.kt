package com.example.nicolas.coursanizer47


import Helpers.convertCourseToString
import Helpers.convertStringToCourse
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.InputType
import android.view.*
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_courses.*
import kotlinx.android.synthetic.main.fragment_courses.view.*
import java.io.File


class CoursesFragment : Fragment() {

    lateinit var myAdapter: Courses_Adapter
    lateinit var searchView: android.support.v7.widget.SearchView
    lateinit var recyclView: RecyclerView

    val splitString = "/newline"

    var courses = ArrayList<CourseDataClass>()
    private var CoursesFull: ArrayList<CourseDataClass>
    init {
        CoursesFull = ArrayList(courses)
    }

    var clicked_course_index = -1
    var clicked_expandable = 0
    var clicked_course_unhighlight = 0


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


        Inflater.btn_showbuttons.setOnClickListener {
            when (clicked_expandable) {
                0 -> {
                    setButtonsVisible("visible")
                    clicked_expandable += 1
                }
                1 -> {
                    setButtonsVisible("gone")
                    clicked_expandable -= 1
                }
            }


        }

        Inflater.button_chartsinglecourse.setOnClickListener {
            clicked_course_unhighlight = myAdapter.clicked_index_for_unhighlight
            clicked_course_index = myAdapter.clicked_index
            val filteredCourses = myAdapter.Courses
            var coursesForStatistics = ArrayList<CourseDataClass>()
            if (clicked_course_index != -1 && clicked_course_unhighlight == 1) {
                val myCourse = listOf(courses.get(clicked_course_index))
                coursesForStatistics = ArrayList<CourseDataClass>(myCourse)
            } else {
                coursesForStatistics = filteredCourses
            }
            val frag = StatisticsFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList("courses", coursesForStatistics)
            frag.setArguments(bundle)
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.fragmentContainer, frag)
            transaction.addToBackStack(null)
            (activity as MainActivity).setNavBarItem("statistics")
            transaction.commit()
        }


        Inflater.button_sharecourse.setOnClickListener {
            clicked_course_index = myAdapter.clicked_index
            clicked_course_unhighlight = myAdapter.clicked_index_for_unhighlight
            if (clicked_course_index != -1 && clicked_course_unhighlight == 1) {
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
            } else {
                Toast.makeText(context, "Tienes que marcar un curso!", Toast.LENGTH_SHORT).show()
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

    inline fun catchAll(action: () -> Unit) {
        try {
            action()
        } catch (t: Throwable) {
            Toast.makeText(context, "No es el formato correcto!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        catchAll {
            if (requestCode == 111 && resultCode == Activity.RESULT_OK) {
                val selectedFile = data?.data
                val inputStream = context!!.getContentResolver().openInputStream(selectedFile)
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                val string_from_file = String(buffer)
                println(string_from_file)
                val newCourseStrings = string_from_file.split(splitString)
                var newCourses: MutableList<CourseDataClass> =
                    newCourseStrings.map { convertStringToCourse(it) }.toMutableList()
                println(newCourses)
                courses = ArrayList(newCourses)
                saveData()
                myAdapter = Courses_Adapter(courses)
                recyclView.adapter = myAdapter
            }
        }
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
            R.id.borrarrtodo -> {
                var m_Text = "";
                val builder = AlertDialog.Builder(context!!)
                builder.setTitle("Para confirmar inserte la palabra 'confirmar'")
                val input = EditText(context!!)
                input.inputType = InputType.TYPE_CLASS_TEXT
                builder.setView(input)
                builder.setPositiveButton(
                    "Confirmar"
                ) {
                    dialog, which -> m_Text = input.text.toString()
                    if (m_Text == "confirmar" || m_Text == "confirmar ") {
                        courses.clear()
                        saveData()
                    }

                }
                builder.setNegativeButton(
                    "Atrás"
                ) { dialog, which -> dialog.cancel() }
                builder.show()
                return true
            }
            R.id.importartodo -> {
                val intent = Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(Intent.createChooser(intent, "Elige backup archivo"), 111)
                return true
            }
            R.id.exportartodo -> {
                var courseStrings: MutableList<String> = courses.map { convertCourseToString(it) }.toMutableList()
                val response = courseStrings.joinToString(splitString)
                context!!.openFileOutput("backup.txt", Context.MODE_PRIVATE).use {
                    it.write(response.toByteArray())
                }
                val file = File(context!!.filesDir, "backup.txt")
                val uri = FileProvider.getUriForFile(context!!, "com.mydomain.myapp.fileprovider", file)
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/*"
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                //startActivityForResult(Intent.createChooser(shareIntent., "Backup"), BACKUP_FILE_REQUEST_CODE)
                startActivity(Intent.createChooser(shareIntent, "share file with"))
                return true;
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun setButtonsVisible(type: String) {
        when(type) {
            "visible" -> {
                button_addcourse.visibility = View.VISIBLE
                button_chartsinglecourse.visibility = View.VISIBLE
                button_sharecourse.visibility = View.VISIBLE
                btn_showbuttons.setBackgroundColor(0x32d81b60)
            }
            "gone" -> {
                button_addcourse.visibility = View.GONE
                button_chartsinglecourse.visibility = View.GONE
                button_sharecourse.visibility = View.GONE
                btn_showbuttons.setBackgroundColor(0xd81b60)
            }
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

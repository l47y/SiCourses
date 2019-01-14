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


class CoursesFragment : Fragment() {

    lateinit var myAdapter: Courses_Adapter
    lateinit var searchView: android.support.v7.widget.SearchView
    lateinit var recyclView: RecyclerView

    var courses = ArrayList<CourseDataClass>()

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
            myAdapter = Courses_Adapter(courses)
            recyclView.adapter = myAdapter
        }

        Inflater.button_addcourse.setOnClickListener {
            val intent = Intent(context, AddCourseDatos::class.java)
            startActivity(intent)
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
                val nameToDelete = courses.get(position).nombre
                val builder = AlertDialog.Builder(context!!)
                val string = "Estás seguro que quieres borrar el curso $nameToDelete ?"
                builder.setTitle("Confirmación necesaria")

                builder.setMessage(string)
                builder.setPositiveButton("Si") { dialog, which ->
                    courses.removeAt(position)
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
                courses = ArrayList(courses.sortedWith(compareBy({ it.media})))
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

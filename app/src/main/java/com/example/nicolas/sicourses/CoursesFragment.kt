package com.example.nicolas.sicourses


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_courses.*
import kotlinx.android.synthetic.main.fragment_courses.view.*


class CoursesFragment : Fragment() {

    lateinit var myAdapter: Courses_Adapter
    lateinit var searchView: android.support.v7.widget.SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val Inflater = inflater.inflate(R.layout.fragment_courses, container, false)

        setHasOptionsMenu(true)

        val recyclView = Inflater.recyclerView_Courses
        recyclView.layoutManager = LinearLayoutManager(context)

        val bundle = getArguments()
        if (bundle != null) {
            val courses = bundle.getParcelableArrayList<CourseDataClass>("courses")
            myAdapter = Courses_Adapter(courses)
            recyclView.adapter = myAdapter
        }

        Inflater.button_addcourse.setOnClickListener {
            val intent = Intent(context, AddCourseDatos::class.java)
            startActivity(intent)
        }

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


}

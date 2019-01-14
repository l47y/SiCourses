package com.example.nicolas.sicourses

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.internal.NavigationMenuItemView
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_courses.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_statistics.*
import android.widget.Toast
import android.R.attr.orientation
import android.content.res.Configuration


class MainActivity : AppCompatActivity() {


    // global variables
    public var courses = mutableListOf<CourseDataClass>() // List of courses
    public var I_COME_FROM = "nowhere" // Indicator from where the activity is started


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item->
        when(item.itemId) {
            R.id.nav_home -> {
                loadData()
                replaceFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_courses -> {
                loadData()
                val frag = CoursesFragment()
                val bundle = Bundle()

                // If coming from AddCourseDatos, than add the new course to courses and send the bundle to the CoursesFragment
                if (I_COME_FROM == "AddCourse") {
                    val intent = getIntent()
                    val nombre = intent.getStringExtra("nombre")
                    val lugar = intent.getStringExtra("lugar")
                    val empresa = intent.getStringExtra("empresa")
                    val evalVec = intent.getStringExtra("evals")
                    val de = intent.getStringExtra("de")
                    val hasta = intent.getStringExtra("hasta")

                    val media = intent.getDoubleExtra("media", 0.toDouble())
                    val newCourse = CourseDataClass(nombre, lugar, empresa, evalVec, de, hasta, media)
                    courses.add(newCourse)
                    saveData()
                } else if (I_COME_FROM == "DeleteCourse") {
//                    val intent = getIntent()
//                    courses = ArrayList<CourseDataClass>()
//                    val coursesFromIntent = intent.getParcelableArrayExtra("courses")
//                    for (course in coursesFromIntent) {
//                        courses.add(course)
//                    }

                }
                val coursesForSending = ArrayList<CourseDataClass>(courses)
                bundle.putParcelableArrayList("courses", coursesForSending)
                frag.setArguments(bundle)
                replaceFragment(frag)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_statistics -> {
                loadData()
                val frag = StatisticsFragment()
                val bundle = Bundle()
                val coursesForSending = ArrayList<CourseDataClass>(courses)
                bundle.putParcelableArrayList("courses", coursesForSending)
                frag.setArguments(bundle)
                replaceFragment(frag)
                return@OnNavigationItemSelectedListener true
            }
            else -> {
                return@OnNavigationItemSelectedListener false
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_clearall.setOnClickListener {
            clearAll()
        }

        //loadData()

        bottom_nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val intent = getIntent()
        val WHERE_DO_I_COME_FROM: String? = intent.getStringExtra("I_COME_FROM")

        // If coming from AddCourseDatos than Start the CoursesFragment, else start the HomeFragment
        if (WHERE_DO_I_COME_FROM == "AddCourse") {
            I_COME_FROM = "AddCourse"
            bottom_nav.setSelectedItemId(R.id.nav_courses)
            I_COME_FROM = "nowhere"
        } else if(WHERE_DO_I_COME_FROM == "DeleteFrom") {
            I_COME_FROM == "DeleteCourse"
            bottom_nav.setSelectedItemId(R.id.nav_courses)
            I_COME_FROM == "nowhere"
        }

        else {
            replaceFragment(HomeFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }

    private fun saveData() {
        val sharedPrefsObj = getSharedPreferences("data", Context.MODE_PRIVATE)
        val editor = sharedPrefsObj.edit()
        val gson = Gson()
        val json = gson.toJson(courses)
        editor.putString("savedCourses", json)
        editor.apply()

    }

    private fun loadData() {
        val sharedPredsObj = getSharedPreferences("data", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPredsObj.getString("savedCourses", null)
        if (json != null) {
            val type = object : TypeToken<ArrayList<CourseDataClass>>() {}.type
            courses = gson.fromJson<ArrayList<CourseDataClass>>(json, type)
        }
    }

    private fun clearAll() {
        val sharedPredsObj = getSharedPreferences("data", Context.MODE_PRIVATE)
        sharedPredsObj.edit().clear().apply()
    }


}


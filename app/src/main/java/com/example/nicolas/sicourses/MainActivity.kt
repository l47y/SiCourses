package com.example.nicolas.sicourses

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    // global variables
    var courses = mutableListOf<CourseDataClass>() // List of courses
    var I_COME_FROM = "nowhere" // Indicator from where the activity is started


    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item->
        when(item.itemId) {
            R.id.nav_courses -> {
                loadData()
                val frag = CoursesFragment()
                val bundle = Bundle()

                if (I_COME_FROM == "AddCourse") {
                    val intent = getIntent()
                    val nombre = intent.getStringExtra("nombre")
                    val lugar = intent.getStringExtra("lugar")
                    val empresa = intent.getStringExtra("empresa")
                    val evalVec = intent.getStringExtra("evals")
                    val de = intent.getStringExtra("de")
                    val hasta = intent.getStringExtra("hasta")

                    val media = intent.getDoubleExtra("media", 0.toDouble())
                    val newCourse = CourseDataClass(nombre, lugar, empresa, evalVec, de, hasta, media, courses.size)
                    courses.add(newCourse)
                    saveData()
                }

                courses = ArrayList(courses.sortedWith(CompareCourses).asReversed())
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
            R.id.nav_map -> {
                val frag = MapFragment()
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

        bottom_nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val intent = getIntent()
        val WHERE_DO_I_COME_FROM: String? = intent.getStringExtra("I_COME_FROM")

        // Check from which activity we come
        I_COME_FROM = when(WHERE_DO_I_COME_FROM)  {
            "AddCourse" -> "AddCourse"
            "DeleteCourse" -> "DeleteCourse"
            else -> "nowhere"
        }
        bottom_nav.setSelectedItemId(R.id.nav_courses)
        I_COME_FROM = "nowhere"
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

    fun setNavBarItem(item: String) {
        when (item) {
            "statistics" -> {
                bottom_nav.setSelectedItemId(R.id.nav_statistics)
            }
            "courses" -> {
                bottom_nav.setSelectedItemId(R.id.nav_courses)
            }
            "map" -> {
                bottom_nav.setSelectedItemId(R.id.nav_map)
            }
        }
    }


}


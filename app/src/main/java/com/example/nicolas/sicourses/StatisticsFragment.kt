package com.example.nicolas.sicourses


import android.os.Bundle
import android.support.design.chip.ChipGroup
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_statistics.view.*


class StatisticsFragment : Fragment() {


    lateinit var container_layout: LinearLayout
    lateinit var mybundle: Bundle


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val Inflater = inflater.inflate(R.layout.fragment_statistics, container, false)

        setHasOptionsMenu(true)

        container_layout = Inflater.findViewById<View>(R.id.chart_container) as LinearLayout
        val args = arguments
        if (args != null) {
            mybundle = args
        }

        makePieChart()

        return Inflater
    }

    private fun makePieChart() {

        if (mybundle != null) {

            var allEvals = ArrayList<Int>()
            var evalNum = ArrayList<String>()
            var howManyofEachEval = ArrayList<Int>()
            var pieDataEntrys = ArrayList<PieEntry>()

            val chart = PieChart(context)
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
            chart.setPadding(16, 16, 16, 16)
            chart.layoutParams = params

            val courses = mybundle.getParcelableArrayList<CourseDataClass>("courses")

            for (course in courses) {
                val evals = course.evals.split(",").dropLast(1).map { it.toInt() }
                allEvals.addAll(evals)
            }
            val valueCounts = allEvals.groupingBy { it }.eachCount()
            evalNum.addAll(valueCounts.map { element -> element.key.toString()})
            howManyofEachEval.addAll(valueCounts.map {element -> element.value})
            for (i in 0..howManyofEachEval.size-1) {
                pieDataEntrys.add(PieEntry(howManyofEachEval.get(i).toFloat(), evalNum.get(i)))
            }
            val set = PieDataSet(pieDataEntrys, "evalDist")
            set.setSliceSpace(7f)
            var colors = ArrayList<Int>()
            for (c in ColorTemplate.VORDIPLOM_COLORS) {
                colors.add(c)
            }
            set.setColors(colors)
            val finalData = PieData(set)
            chart.setData(finalData)
            chart.animateY(1200)
            chart.invalidate()

            container_layout.addView(chart)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.chartnav_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.charnav_back -> {
                Toast.makeText(context, "back clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.charnav_foward -> {
                Toast.makeText(context, "foward clicked", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }



}

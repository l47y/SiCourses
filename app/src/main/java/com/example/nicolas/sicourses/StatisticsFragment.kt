package com.example.nicolas.sicourses


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_statistics.view.*


class StatisticsFragment : Fragment() {

    var allEvals = ArrayList<Int>()
    var evalNum = ArrayList<String>()
    var howManyofEachEval = ArrayList<Int>()
    var pieDataEntrys = ArrayList<PieEntry>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val Inflater = inflater.inflate(R.layout.fragment_statistics, container, false)

        val chart = Inflater.piechart

        val bundle = getArguments()
        if (bundle != null) {
            val courses = bundle.getParcelableArrayList<CourseDataClass>("courses")

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


        }



        return Inflater
    }


}

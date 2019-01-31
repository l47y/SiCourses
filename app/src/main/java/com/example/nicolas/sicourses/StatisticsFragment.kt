package com.example.nicolas.sicourses


import android.graphics.Canvas
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.fragment_statistics.*
import kotlinx.android.synthetic.main.fragment_statistics.view.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.zip.Inflater


class StatisticsFragment : Fragment() {

    lateinit var container_layout: LinearLayout
    lateinit var mybundle: Bundle
    lateinit var myInflater: View

    var courses = ArrayList<CourseDataClass>()

    val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.MATCH_PARENT)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myInflater = inflater.inflate(R.layout.fragment_statistics, container, false)

        setHasOptionsMenu(true)
        setRetainInstance(true)

        container_layout = myInflater.findViewById<View>(R.id.chart_container) as LinearLayout
        val args = arguments
        if (args != null) {
            mybundle = args
        }

        makePieChart()

        return myInflater
    }

    private fun makeBarChart() {
        if (mybundle != null) {

            container_layout.removeAllViews()

            val chart = BarChart(context)
            chart.setDrawGridBackground(true)
            chart.setPadding(16, 16, 16, 16)
            chart.layoutParams = params

            var barDataEntrys = ArrayList<BarEntry>()
            var axisLabels = ArrayList<String>()
            courses = mybundle.getParcelableArrayList<CourseDataClass>("courses")
            courses = ArrayList(courses.sortedWith(compareBy({ it.media })).asReversed())

            var numberTopCourses = 5
            if (courses.size > numberTopCourses) courses = ArrayList(courses.dropLast(courses.size - numberTopCourses))

            for (i in 0..courses.size - 1) {
                val media = courses.get(i).media.toFloat()
                val nombre = courses.get(i).nombre
                val empresa = courses.get(i).empresa
                val lugar = courses.get(i).lugar
                barDataEntrys.add(BarEntry(i.toFloat(), media))
                val axisString = "$empresa, $lugar"
                axisLabels.add(axisString)
            }

            val set = BarDataSet(barDataEntrys, "bestCourses")
            set.setColors(intArrayOf(R.color.gradient8), context)
            val xAxis = chart.xAxis
            val axisRight = chart.axisRight
            val axisLeft = chart.axisLeft

            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
            xAxis.setLabelRotationAngle(-90.toFloat())
            xAxis.setDrawGridLines(false)
            xAxis.setLabelCount(axisLabels.size)

            axisLeft.isEnabled = false
            axisRight.isEnabled = false

            xAxis.setValueFormatter { value, axis ->
                val index = value.toInt()
                axisLabels.get(index)
            }

            val finalData = BarData(set)
            chart.setData(finalData)
            chart.animateY(1200)
            chart.setScaleEnabled(false)
            chart.setDrawGridBackground(false)

            chart.legend.isEnabled = false
            chart.description.text = ""
            myInflater.chart_title.text = "Top " + numberTopCourses.toString() + " Cursos según Media"
            chart.marker = myMarkerView()


            chart.invalidate()
            container_layout.addView(chart)
        }
    }

    private fun makePieChart() {

        if (mybundle != null) {

            container_layout.removeAllViews()

            var allEvals = ArrayList<Int>()
            var evalNum = ArrayList<String>()
            var howManyofEachEval = ArrayList<Int>()
            var pieDataEntrys = ArrayList<PieEntry>()

            val chart = PieChart(context)
            chart.setPadding(16, 16, 16, 16)
            chart.layoutParams = params

            courses = mybundle.getParcelableArrayList<CourseDataClass>("courses")

            for (course in courses) {
                val evals = course.evals.split(",").dropLast(1).map { it.toInt() }
                allEvals.addAll(evals)
            }

            val totalMedia = allEvals.average()

            val valueCounts = allEvals.groupingBy { it }.eachCount()
            evalNum.addAll(valueCounts.map { element -> element.key.toString() })
            howManyofEachEval.addAll(valueCounts.map { element -> element.value })
            for (i in 0..howManyofEachEval.size - 1) {
                pieDataEntrys.add(PieEntry(howManyofEachEval.get(i).toFloat(), evalNum.get(i)))
            }
            val set = PieDataSet(pieDataEntrys, "evalDist")
            set.setSliceSpace(7f)

            set.setColors(
                intArrayOf(
                    R.color.gradient10,
                    R.color.gradient9,
                    R.color.gradient8,
                    R.color.gradient7,
                    R.color.gradient6,
                    R.color.gradient5
                ), context
            )

            val finalData = PieData(set)
            finalData.setValueTextColor(R.color.primary_dark_material_dark)
            chart.setData(finalData)
            chart.animateY(1200)
            chart.legend.isEnabled = false
            chart.description.text = ""
            myInflater.chart_title.text = "Distribución de Evaluaciones"
            chart.centerText = "Media Total: " + roundOnDecimal(totalMedia) + "\n" +
                    "Número Cursos: " + courses.size.toString()

            chart.marker = myMarkerView()
            chart.invalidate()
            container_layout.addView(chart)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.chartnav_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.charnav_back -> {
                makePieChart()
            }
            R.id.charnav_foward -> {
                makeBarChart()
            }
        }
        return true
    }

    private fun roundOnDecimal(double: Double): String {
        val df = DecimalFormat("#.###")
        df.roundingMode = RoundingMode.CEILING
        return df.format(double)
    }

    private fun roundOnInteger(double: Double): String {
        val df = DecimalFormat("#")
        df.roundingMode = RoundingMode.CEILING
        return df.format(double)
    }

    inner class myMarkerView: IMarker {
        override fun getOffset(): MPPointF {
            return(MPPointF(5f, 8f))
        }

        override fun refreshContent(e: Entry?, highlight: Highlight?) {
            if (e is PieEntry) {
                var label = ""
                if (e.value.toInt() == 1) label = when (e.label) {
                    "10" -> "diez"
                    "9" -> "nueve"
                    "8" -> "ocho"
                    "7" -> "siete"
                    "6" -> "seis"
                    "5" -> "cinco"
                    else -> {
                        "Otros"
                    }
                } else label = when (e.label) {
                    "10" -> "dieces"
                    "9" -> "nueves"
                    "8" -> "ochos"
                    "7" -> "sietes"
                    "6" -> "seises"
                    "5" -> "cincos"
                    else -> {
                        "Otros"
                    }
                }
                val message = "Tienes " + roundOnInteger(e.value.toDouble()) + " " + label + " en total!"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            } else if(e is BarEntry) {
                val courseIndex = e.x.toInt()
                val Course = courses.get(courseIndex)
                val message = "Curso: " + Course.nombre +  ", " + Course.de + "-" + Course.hasta
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }

        override fun draw(canvas: Canvas?, posX: Float, posY: Float) {
           // Toast.makeText(context, highlight.toString(), Toast.LENGTH_SHORT).show()

        }

        override fun getOffsetForDrawingAtPoint(posX: Float, posY: Float): MPPointF {
            return(MPPointF(3f, 4f))
        }
    }

}




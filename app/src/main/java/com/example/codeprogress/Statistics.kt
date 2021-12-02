package com.example.codeprogress

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.codeprogress.Responses.Response_codeforces_rating
import com.example.codeprogress.Responses.Response_codeforces_statistics
import com.example.codeprogress.Responses.Response_leetcode_statistics
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.android.synthetic.main.activity_statistics.*
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel


class Statistics : AppCompatActivity() {
    var pieChart:PieChart? = null
    var codeforces_statistics_url = "http://143.244.130.232:3000/api/codeforces/statistics?cdf="
    var leetcode_statistics_url = "http://143.244.130.232:3000/api/leetcode/statistics?leet="
    var codeforces_rating_url = "http://143.244.130.232:3000/api/codeforces/rating?cdf="
    var leetcode_statistics:Leetcode_statistics? = null
    var codeforces_statistics:Codeforces_statistics? = null
    val bar_color = -0xa9480f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        pieChart  = piechart
        val cdf = intent.getStringExtra("codeforces").toString()
        val leet = intent.getStringExtra("leetcode").toString()
        codeforces_statistics_url+=cdf
        leetcode_statistics_url+=leet
        codeforces_rating_url+=cdf
        fetch_data(cdf,leet)


    }

    fun fetch_data(cdf:String,leet:String){
        if(cdf!="") {
            api<Response_codeforces_statistics>(
                codeforces_statistics_url,
                Response_codeforces_statistics(), this, ::cdf_statistics_callback
            ).fetch()
            api<Response_codeforces_rating>(codeforces_rating_url,
            Response_codeforces_rating(0),this,::cdf_rating_callback).fetch()
        }
        if(leet!="") {
            api<Response_leetcode_statistics>(
                leetcode_statistics_url,
                Response_leetcode_statistics(), this, ::leet_statistics_callback
            ).fetch()
        }
    }

    private fun cdf_rating_callback(responseCodeforcesRating: Response_codeforces_rating) {
         cdf_rating.text = responseCodeforcesRating.rating.toString()
    }

    fun cdf_statistics_callback(response: Response_codeforces_statistics){
        var levels:HashMap<String,Int> = HashMap<String,Int>()
        var ratings:HashMap<Int,Int> = HashMap<Int,Int>()
        Log.d("fuck","fetching")
        for(question in response) {
            if (levels[question.level[0].toString()] == null) {
                levels[question.level[0].toString()] = 1
            } else {
                levels[question.level[0].toString()] = levels[question.level[0].toString()]!! + 1
            }
            if(ratings[question.rating]==null){
                ratings[question.rating] = 1
            }
            else{
                ratings[question.rating] =  ratings[question.rating]!! + 1
            }
        }
        codeforces_statistics = Codeforces_statistics(levels,ratings)
        set_data_codeforces()

    }

    fun leet_statistics_callback(response: Response_leetcode_statistics){
       leetcode_statistics = Leetcode_statistics(response[1].count,response[2].count,
           response[3].count)
        set_data_leetcode()

    }


    fun set_data_leetcode(){

        easy.setText(Integer.toString(leetcode_statistics!!.easy))
        medium.setText(Integer.toString(leetcode_statistics!!.medium))
        hard.setText(Integer.toString(leetcode_statistics!!.hard))

        pieChart!!.addPieSlice(
            PieModel(
                "R", easy.text.toString().toInt().toFloat(),
                Color.parseColor("#FFA726")
            )
        )
        pieChart!!.addPieSlice(
            PieModel(
                "Python", medium.text.toString().toInt().toFloat(),
                Color.parseColor("#66BB6A")
            )
        )
        pieChart!!.addPieSlice(
            PieModel(
                "C++", hard.text.toString().toInt().toFloat(),
                Color.parseColor("#EF5350")
            )
        )

        pieChart!!.startAnimation();
    }

    fun set_data_codeforces(){
        set_rating_graph()
        set_levels_graph()

    }
    
    fun set_levels_graph(){
        var xaxis = 0f
        var barEntries = ArrayList<BarEntry>()
        for((key,value) in codeforces_statistics!!.levels){
            barEntries.add(BarEntry(xaxis,value.toFloat()))
            xaxis++
        }

        val barDataSet = BarDataSet(barEntries,"No. of Questions")
        val xlabels = ArrayList<String>()
        for((key,value) in codeforces_statistics!!.levels){
            xlabels.add(key)
        }
        Log.d("fuck",xlabels.toString())
        val barData = BarData(barDataSet)
        barDataSet.setColor(bar_color)
        levelsBarChart.xAxis.valueFormatter = IndexAxisValueFormatter(xlabels)
        levelsBarChart.setData(barData)
        levelsBarChart.description = null
        levelsBarChart.notifyDataSetChanged()
        levelsBarChart.invalidate()
    }
    
    fun set_rating_graph(){
        var xaxis = 0f
        var barEntries = ArrayList<BarEntry>()
        var pairList = ArrayList<Pair<Float,Int>>()
        for((key,value) in codeforces_statistics!!.ratings){
            pairList.add(Pair<Float,Int>(key.toFloat(),value))
        }
        pairList.sortBy{it.first}
        Log.d("fuck",pairList.toString())
        for(value in pairList){
            barEntries.add(BarEntry(xaxis,value.second.toFloat()))
            xaxis++
        }

        val barDataSet = BarDataSet(barEntries,"No. of Questions")
        val xlabels = ArrayList<String>()
        for(value in pairList){
            xlabels.add(value.first.toString())
        }
        Log.d("fuck",xlabels.toString())
        val barData = BarData(barDataSet)
        barDataSet.setColor(bar_color)
        ratingBarChart.xAxis.valueFormatter = IndexAxisValueFormatter(xlabels)
        ratingBarChart.setData(barData)
        ratingBarChart.description = null
        ratingBarChart.notifyDataSetChanged()
        ratingBarChart.invalidate()
    }
}
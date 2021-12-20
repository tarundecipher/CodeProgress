package com.example.codeprogress


import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codeprogress.Responses.Response_codeforces_submissions
import com.example.codeprogress.Responses.Response_leetcode_submissions
import com.example.codeprogress.Responses.ResultXX
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.android.synthetic.main.activity_friend_questions.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.getFriend
import kotlinx.android.synthetic.main.activity_track_friend.*
import java.text.SimpleDateFormat
import java.util.*

class FriendQuestions : AppCompatActivity() {
    private var codeforces_submission_url = "http://192.168.1.7:3000/api/codeforces/submissions?cdf="
    private var leetcode_submission_url = "http://192.168.1.7:3000/api/leetcode/submissions?leet="
    private var result:com.example.codeprogress.Responses.Result? = null
    private var submission_list:MutableList<Submissions> = mutableListOf()
    private var result2:List<ResultXX>? = null
    private var leetcode = ""
    private var codeforces = ""
    private var to_be_fetched = 0
    private var mInterstitialAd: InterstitialAd? = null
    private var adapter:friendQuestionsAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_questions)

        MobileAds.initialize(this) {
            Log.d("fuck","ads initialized")
        }
        create_interstital_ad(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = friendQuestionsAdapter(submission_list)
        recyclerView.adapter = adapter

        swipeRefreshfriend.isRefreshing = true
        addSwipeRefresh()
        initalSetup()

    }

    fun create_interstital_ad(activity: Activity){

        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,"ca-app-pub-1007368186384060/8638321230", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("fuck", adError?.message)
                mInterstitialAd = null

            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d("fuck", "Ad was loaded.")
                mInterstitialAd = interstitialAd
                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(activity)
                } else {
                    Log.d("fuck", "The interstitial ad wasn't ready yet.")
                }
                mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Log.d("fuck", "Ad was dismissed.")

                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                        Log.d("fuck", "Ad failed to show.")

                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d("fuck", "Ad showed fullscreen content.")
                        mInterstitialAd = null
                    }
                }
            }
        })

    }

    private fun addSwipeRefresh() {
        swipeRefreshfriend.setOnRefreshListener {
            submission_list.clear()
            if(leetcode.length>0 && codeforces.length>0){
                to_be_fetched = 2;
            }
            else if(leetcode.length>0){
                to_be_fetched = 1;
            }
            else if(codeforces.length>0){
                to_be_fetched = 1;
            }

            if(to_be_fetched>0) {
                fetch_submissions()
            }

        }
    }

    private fun initalSetup(){
        leetcode = intent.getStringExtra("leetcode").toString()
        codeforces = intent.getStringExtra("codeforces").toString()

        if(leetcode==null){
            leetcode = ""
        }
        if(codeforces==null){
            codeforces = ""
        }
        codeforces_submission_url+=codeforces
        leetcode_submission_url+=leetcode
        if(leetcode.length>0 && codeforces.length>0){
            to_be_fetched = 2;
        }
        else if(leetcode.length>0){
            to_be_fetched = 1;
        }
        else if(codeforces.length>0){
            to_be_fetched = 1;
        }

        if(to_be_fetched>0) {
            fetch_submissions()
        }
    }

    fun fetch_submissions() {
        if (codeforces.length > 0) {
            api<Response_codeforces_submissions>(
                codeforces_submission_url,
                Response_codeforces_submissions(result), this, ::codeforces_submission_callback
            ).fetch()
        }
        if (leetcode.length > 0) {
            api<Response_leetcode_submissions>(
                leetcode_submission_url,
                Response_leetcode_submissions(result2), this, ::leetcode_submission_callback
            ).fetch()
        }
    }

    private fun leetcode_submission_callback(responseLeetcodeSubmissions: Response_leetcode_submissions) {
        for (submission in responseLeetcodeSubmissions.result!!){
            submission_list.add(
                Submissions(submission.title,submission.statusDisplay,
                "leetcode",submission.timestamp)
            )
        }
        callback()
    }

    private fun codeforces_submission_callback(responseCodeforcesSubmissions: Response_codeforces_submissions) {
        for (submission in responseCodeforcesSubmissions.result!!.result){
            submission_list.add(
                Submissions(submission.problem.name,submission.verdict,
            "codeforces",submission.creationTimeSeconds.toString())
            )
        }
        callback()
    }

    private fun callback(){
        to_be_fetched--
        if(to_be_fetched==0) {
            submission_list.sortByDescending { it.time }
            beautify()
            adapter!!.notifyDataSetChanged()
            swipeRefreshfriend.isRefreshing = false
        }
    }

    private fun beautify() {
        for (i in 0 until submission_list.size) {
            if (submission_list[i].verdict == "Accepted" ||
                submission_list[i].verdict == "OK"
            ) {
                submission_list[i].verdict = "Accepted"
            } else {
                submission_list[i].verdict = "Wrong Answer"
            }

                val currentdate = Date()
                var final_time = ""
                val milliseconds = currentdate.time - submission_list[i].time.toLong() * 1000
                val seconds = milliseconds / 1000
                val minutes = seconds / 60
                val hours = minutes / 60
                val days = hours / 24
                if (days >= 1) {
                    final_time = days.toString() + " days ago"
                } else if (hours >= 1) {
                    final_time = hours.toString() + " hours ago"
                } else if (minutes >= 1) {
                    final_time = minutes.toString() + " minutes ago"
                } else {
                    final_time = "Just now"
                }

                submission_list[i].time = final_time
            }


    }

    data class Submissions(val name:String, var verdict:String, val platform:String, var time:String)

}
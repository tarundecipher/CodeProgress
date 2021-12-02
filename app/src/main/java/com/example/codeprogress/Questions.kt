package com.example.codeprogress

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.codeprogress.Responses.Response_questions
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_questions.*
import java.lang.Exception

class Questions : AppCompatActivity() {
    var codechef:String = ""
    var codeforces:String = ""
    var leetcode:String = ""
    var codeforces_questions_url = "http://143.244.130.232:3000/api/codeforces/questions?cdf="
    var codechef_questions_url = "http://143.244.130.232:3000/api/codechef/questions?cdchf="
    var leetcode_questions_url = "http://143.244.130.232:3000/api/leetcode/questions?leet="
    private var mInterstitialAd: InterstitialAd? = null
    var my_intent:Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)
        my_intent = Intent(this,Statistics::class.java)
        codechef = intent.getStringExtra("codechef").toString()
        codeforces = intent.getStringExtra("codeforces").toString()
        leetcode = intent.getStringExtra("leetcode").toString()
        if(codechef==null){
            codechef = ""
        }
        if(codeforces==null){
            codeforces = ""
        }
        if(leetcode==null){
            leetcode = ""
        }
        MobileAds.initialize(this) {
            Log.d("fuck","ads initialized")
        }
        create_interstital_ad(this)
        codeforces_questions_url+=codeforces
        codechef_questions_url+=codechef
        leetcode_questions_url+=leetcode

        attach_listener()
        fetch_details()
    }

    private fun attach_listener() {


        get_statistics.setOnClickListener{
            val leet = leetcode
            val forces =  codeforces
                my_intent!!.putExtra("codeforces", forces)
                my_intent!!.putExtra("leetcode", leet)
            Log.d("fuck",my_intent.toString())
                startActivity(my_intent)
            }
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

    fun fetch_details(){

           leetcode2.visibility = View.GONE
           codechef2.visibility = View.GONE
           codeforces2.visibility = View.GONE
            var api:Any =0
            if(codeforces.length!=0) {
                try {
                    api<Response_questions>(
                        codeforces_questions_url, Response_questions(0),
                        this, ::get_questions_result_codeforces
                    ).fetch()
                }
                catch(err:Exception){
                    codeforces2.visibility = View.VISIBLE
                    cdf_progress.visibility = View.GONE
                }
            }
        else{
                codeforces2.visibility = View.VISIBLE
                cdf_progress.visibility = View.GONE
            }
            if(leetcode.length!=0) {
                try {
                    api<Response_questions>(
                        leetcode_questions_url, Response_questions(0),
                        this, ::get_questions_result_leetcode
                    ).fetch()
                }
                catch(err:Exception){
                    leet_progress.visibility = View.GONE
                    leetcode2.visibility = View.VISIBLE
                }
            }
        else{
                leet_progress.visibility = View.GONE
                leetcode2.visibility = View.VISIBLE
            }
            if(codechef.length!=0) {
                try {
                    api<Response_questions>(
                        codechef_questions_url, Response_questions(0),
                        this, ::get_questions_result_codechef
                    ).fetch()
                }
                catch(err:Exception){
                    cdchf_progress.visibility = View.GONE
                    codechef2.visibility = View.VISIBLE
                }
            }
        else{
                cdchf_progress.visibility = View.GONE
                codechef2.visibility = View.VISIBLE
            }
    }

    private fun get_questions_result_codechef(responseQuestions: Response_questions) {
        val problems = responseQuestions.problems
        codechef2.text = problems.toString()
        cdchf_progress.visibility = View.GONE
        codechef2.visibility = View.VISIBLE
        total.text = (total.text.toString().toInt()+problems).toString()
    }

    private fun get_questions_result_leetcode(responseQuestions: Response_questions) {
        val problems = responseQuestions.problems
        leetcode2.text = problems.toString()
        leet_progress.visibility = View.GONE
        leetcode2.visibility = View.VISIBLE
        total.text = (total.text.toString().toInt()+problems).toString()
    }

    private fun get_questions_result_codeforces(responseQuestions: Response_questions) {
        val problems = responseQuestions.problems
        codeforces2.text = problems.toString()
        codeforces2.visibility = View.VISIBLE
        cdf_progress.visibility = View.GONE
        total.text = (total.text.toString().toInt()+problems).toString()
    }


}
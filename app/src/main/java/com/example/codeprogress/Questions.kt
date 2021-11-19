package com.example.codeprogress

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.android.synthetic.main.activity_questions.*
import java.lang.Exception

class Questions : AppCompatActivity() {
    var codechef:String = ""
    var codeforces:String = ""
    var leetcode:String = ""
    private var mInterstitialAd: InterstitialAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)
        codechef = intent.getStringExtra("codechef").toString()
        codeforces = intent.getStringExtra("codeforces").toString()
        leetcode = intent.getStringExtra("leetcode").toString()
        MobileAds.initialize(this) {
            Log.d("fuck","ads initialized")
        }
        create_interstital_ad(this)

        fetch_details()
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

            detailsLayout.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            val api = api(codeforces, codechef, leetcode, this, ::get_result)
            api.fetch()
    }

    fun get_result(response:Response_details){
        if(response==null){
            return;
        }
        try {
            var q1 = 0
            var q2 = 0
            var q3 = 0
            if (response.codeforces!!.length != 0) {
                 q1 = response.codeforces!!.toInt()
                codeforces2.text = response.codeforces
            }
            if (response.codechef!!.size > 0 && (response.codechef!![0].length != 0 ||
                        response.codechef!![1].length != 0)
            ) {
                if (response.codechef!![0].length != 0) {
                    q2 = response.codechef!![0].toInt()
                }
                if (response.codechef!![1].length != 0) {
                    q2 += response.codechef!![1].toInt()
                }
                codechef2.text = (q2).toString()
            }

            if (response.leetcode!!.length != 0) {
                q3 = response.leetcode!!.toInt()
                leetcode2.text = response.leetcode
            }

            total.text = (q1 + q2 + q3).toString()
            progressBar.visibility = View.GONE
            detailsLayout.visibility = View.VISIBLE
        }
        catch(e:Exception){
            progressBar.visibility = View.GONE
            Toast.makeText(this,"Server Error",Toast.LENGTH_LONG).show()
        }
    }
}
package com.example.codeprogress

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_questions.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
     var sharedPref:SharedPreferences? = null

    private var my_intent:Intent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        my_intent = Intent(this,Questions::class.java)


        sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        val chef = sharedPref!!.getString("codechef","")
        val forces = sharedPref!!.getString("codeforces","")
        val leet = sharedPref!!.getString("leetcode","")
        if(chef!!.length>0){
            codechef.setText(chef)
            codeforces.setText(forces)
            leetcode.setText(leet)
        }
        attach_listener()
    }
    private fun attach_listener() {

//        intent = Intent(this,Questions::class.java)
        getDetails.setOnClickListener{

            val chef = codechef.text.toString().trim()
            val forces =  codeforces.text.toString().trim()
            val leet = leetcode.text.toString().trim()
            if(chef.length>0 && forces.length>0 && leet.length>0) {
                with(sharedPref!!.edit()){
                    putString("codechef",chef)
                    putString("codeforces",forces)
                    putString("leetcode",leet)
                    apply()
                }
                my_intent!!.putExtra("codechef", chef)
                my_intent!!.putExtra("codeforces", forces)
                my_intent!!.putExtra("leetcode", leet)
                startActivity(my_intent)

            }
            else{
                Toast.makeText(this,"Fill all usernames",Toast.LENGTH_SHORT).show()
            }
        }
    }


}
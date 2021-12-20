package com.example.codeprogress

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_track_friend.*
import kotlinx.android.synthetic.main.activity_track_progress.*

class TrackFriend : AppCompatActivity() {
    private var my_intent:Intent? = null
    var sharedPref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_friend)
        my_intent = Intent(this,FriendQuestions::class.java)
        sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        val forces = sharedPref!!.getString("codeforces_friend","")
        val leet = sharedPref!!.getString("leetcode_friend","")

        if(forces!!.length>0 || leet!!.length>0){
            codeforces_friend.setText(forces)
            leetcode_friend.setText(leet)
        }
//        Log.d("fuck",sharedPref.toString())
        attach_listener()
    }

    private fun attach_listener() {
        getFriendProgress.setOnClickListener{
            val forces =  codeforces_friend.text.toString().trim()
            val leet = leetcode_friend.text.toString().trim()
            with(sharedPref!!.edit()){
                putString("codeforces_friend",forces)
                putString("leetcode_friend",leet)
                apply()
            }
            my_intent!!.putExtra("codeforces", forces)
            my_intent!!.putExtra("leetcode", leet)
            startActivity(my_intent)
        }
    }
}
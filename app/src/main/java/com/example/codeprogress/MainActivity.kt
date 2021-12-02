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
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_questions.*
import java.lang.Exception
import android.content.DialogInterface

import android.content.ActivityNotFoundException
import com.google.android.material.theme.overlay.MaterialThemeOverlay


import android.app.AlertDialog
import android.net.Uri
import android.view.ContextThemeWrapper

import com.google.android.play.core.appupdate.AppUpdateInfo

import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
     var sharedPref:SharedPreferences? = null

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private var my_intent:Intent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        update_app()
        firebaseAnalytics = Firebase.analytics
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

    fun update_app() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)

// Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

// Checks whether the platform allows the specified type of update,
// and checks the update priority.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.updatePriority() >= 4 /* high priority */
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    1234)
            }
        }
    }

    private fun attach_listener() {

//        intent = Intent(this,Questions::class.java)
        getDetails.setOnClickListener{

            val chef = codechef.text.toString().trim()
            val forces =  codeforces.text.toString().trim()
            val leet = leetcode.text.toString().trim()
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
    }


}
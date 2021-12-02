package com.example.codeprogress

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.android.volley.DefaultRetryPolicy
import com.google.gson.GsonBuilder


class api<T>(val url:String,val response_structure:T,val context:Context,
          val callback:(input:T)->Unit){
    val RequestQueue = Volley.newRequestQueue(context)

    fun fetch(){
        val request = StringRequest(Request.Method.GET,url, Response.Listener { response->
            val gsonBuilder = GsonBuilder()
            val gson  = gsonBuilder.create()
            val details = gson.fromJson(response,response_structure!!::class.java)
            Log.d("fuck",response.toString())
            callback(details)

        }, Response.ErrorListener { error->Log.d("fuck",error.toString()) })
        request.setRetryPolicy(
            DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )
        RequestQueue.add(request)
    }
}
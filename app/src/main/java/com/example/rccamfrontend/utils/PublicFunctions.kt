package com.example.rccamfrontend.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import java.nio.charset.Charset


fun generateToast(context: Activity, text: String, dur: Int = Toast.LENGTH_SHORT){
    Toast.makeText(context, text, dur).show()
}

fun generateSnack(view: View, text: String, dur: Int = Snackbar.LENGTH_SHORT, anch: View? = null){
    val snack = Snackbar.make(view, text, dur)
    if (anch != null){
        snack.anchorView = anch
    }
    snack.show()
}

fun incrementTextView(view: TextView, amount: Float){
    if (view.text.toString() != ""){
        // val intText = Integer.parseInt(view.text.toString())
        val numText = view.text.toString().toFloat()
        view.text = (numText + amount).toString()
    }
}

fun requestPOST(ctx: Context, url: String, data: String = ""){
    // Instantiate the RequestQueue
    val queue = Volley.newRequestQueue(ctx)

    // String request for the provided URL with given data
    val strReq: StringRequest = object : StringRequest(Method.POST, url,
        { },
        { error ->
            Log.d("Volley", "$error")
        }
    ){
        override fun getBody(): ByteArray{
            return data.toByteArray(Charset.defaultCharset())
        }
    }

    // Add the request to the RequestQueue.
    queue.add(strReq)
}

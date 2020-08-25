package com.example.rccamfrontend

import android.app.Activity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun generateToast(context: Activity, text: String, dur: Int = Toast.LENGTH_SHORT){
    Toast.makeText(context, text, dur).show()
}

fun generateSnack(view: View, text: String, dur: Int = Snackbar.LENGTH_SHORT){
    Snackbar.make(view, text, dur).show()
}

fun incrementTextView(view: TextView, amount: Int){
    if (view.text.toString() != ""){
        val intText = Integer.parseInt(view.text.toString())
        view.text = (intText + amount).toString()
    }
}
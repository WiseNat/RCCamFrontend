package com.example.rccamfrontend.utils

import android.app.Activity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

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

fun incrementTextView(view: TextView, amount: Int){
    if (view.text.toString() != ""){
        val intText = Integer.parseInt(view.text.toString())
        view.text = (intText + amount).toString()
    }
}
package com.example.rccamfrontend

import android.app.Activity
import android.widget.Toast

fun generateToast(context: Activity, text: String, dur: Int = Toast.LENGTH_SHORT){
    Toast.makeText(context, text, dur).show()
}
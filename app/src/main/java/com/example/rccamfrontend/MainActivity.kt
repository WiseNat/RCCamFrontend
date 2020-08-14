package com.example.rccamfrontend

import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val typeface = Typeface.createFromAsset(assets, "fonts/agencyb.ttf")

        textfieldIP.typeface = typeface
        textfieldPort.typeface = typeface
        btnConnect.typeface = typeface

    }
}
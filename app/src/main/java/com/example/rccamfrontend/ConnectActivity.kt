package com.example.rccamfrontend

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_connect.*

class ConnectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect)

        val typeface = Typeface.createFromAsset(assets, "fonts/AGENCYR.TTF")

        textfieldIP.typeface = typeface
        textfieldPort.typeface = typeface
        btnConnect.typeface = typeface


        btnConnect.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
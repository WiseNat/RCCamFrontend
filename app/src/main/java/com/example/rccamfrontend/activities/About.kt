package com.example.rccamfrontend.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.rccamfrontend.BuildConfig
import com.example.rccamfrontend.R
import kotlinx.android.synthetic.main.activity_about.*


class About : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        // Modifying Action Bar title
        supportActionBar?.title = "About"

        // Getting IP and Port
        val ip = intent.getStringExtra("ip")
        val port = intent.getStringExtra("port")

        // Setting text attribute of text views
        textViewNet.text = getString(R.string.ip_port, ip, port)
        textViewVer.text = getString(R.string.version, BuildConfig.VERSION_NAME)
    }
}
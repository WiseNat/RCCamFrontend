package com.example.rccamfrontend

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        // Modifying Action Bar
        supportActionBar?.title = "About"

        // Modifying Text views
        val textViewNet = findViewById<View>(R.id.textViewNet) as TextView
        val textViewVer = findViewById<View>(R.id.textViewVer) as TextView

        val ip = intent.getStringExtra("ip")
        val port = intent.getStringExtra("port")
        textViewNet.text = getString(R.string.ip_port, ip, port)

        textViewVer.text = getString(R.string.version, BuildConfig.VERSION_NAME)
    }
}
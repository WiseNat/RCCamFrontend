package com.example.rccamfrontend.activities

import android.os.Bundle
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
        var ip: String? = intent.getStringExtra("ip")
        var port: String? = intent.getStringExtra("port")

        if (ip == null){
           ip = "Unknown"
        }

        if (port == null){
            port = "Unknown"
        }

        // Getting Version
        var build = BuildConfig.VERSION_NAME

        if (build == ""){
            build = "XX.XX.XX"
        }

        // Setting text attribute of text views
        textViewNet.text = getString(R.string.ip_port, ip, port)
        textViewVer.text = getString(R.string.version, build)
    }
}
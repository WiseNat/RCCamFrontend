package com.example.rccamfrontend.activities

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.rccamfrontend.R
import com.example.rccamfrontend.utils.Address
import com.example.rccamfrontend.utils.generateSnack
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_connect.*
import java.util.*

class Connect : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect)

        // Modifying Action Bar title
        supportActionBar?.title = "Connect to RPI"

        // Getting view
        val view = connectConstraint

        btnConnect.setOnClickListener{
            val ip = textfieldIP.text.toString()
            val port = textfieldPort.text.toString()

            // Instantiating User Inputted Address
            val address = Address(ip, port.toDoubleOrNull())


            // IP and Port presence
            var textfieldsPresent = false
            var message = ""
            if (address.emptyIP && address.emptyPort) {
                message = "Enter an IP Address and Port number. "
            } else if (address.emptyIP) {
                message = "Enter an IP Address. "
            } else if (address.emptyPort) {
                message = "Enter a Port number. "
            } else {
                textfieldsPresent = true
            }

            // IP and Port Validity
            var textfieldsValid = false
            if (!address.validIP && !address.validPort) {
                message += "Invalid IP Address and Port number."
            } else if (!address.validIP) {
                message += "Invalid IP Address."
            } else if (!address.validPort) {
                message += "Invalid Port."
            } else {
                textfieldsValid = true
            }

            if (!textfieldsPresent || !textfieldsValid){  // Invalid textfield entries
                generateSnack(view, message, dur = Snackbar.LENGTH_LONG)
            } else {  // Process for going to Main activity if text view inputs are valid
                // Webview instantiating and overriding
                hiddenWebview.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        val intent = Intent(this@Connect, Main::class.java)
                        intent.putExtra("ip", textfieldIP.text.toString())
                        intent.putExtra("port", textfieldPort.text.toString())

                        startActivity(intent)
                    }
                }
            }

            hiddenWebview.loadUrl("http://%s:%s/".format(ip, port))
        }
    }
}

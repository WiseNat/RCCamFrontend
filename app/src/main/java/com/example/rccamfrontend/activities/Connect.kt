package com.example.rccamfrontend.activities

import android.content.Intent
import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.rccamfrontend.R
import com.example.rccamfrontend.utils.Address
import com.example.rccamfrontend.utils.generateSnack
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_connect.*

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

            var nextActivity = false

            // Address logic
            if (address.hasEmptyIP()) {
                generateSnack(view, "Enter an IP Address")
            } else if (!address.hasValidIP()) {
                generateSnack(view, "Invalid IP Address")
            } else if (address.hasEmptyPort()) {
                generateSnack(view, "Enter a Port number")
            } else if (!address.hasValidPort()) {
                generateSnack(view, "Invalid Port")
            } else {
                nextActivity = true
            }

            // Process for going to Main activity if text view inputs are valid
            if (nextActivity){
                var webviewError = false

                // Webview instantiating and overriding
                hiddenWebview.webViewClient = object : WebViewClient() {
                    override fun onReceivedError(
                        view: WebView,
                        request: WebResourceRequest,
                        error: WebResourceError
                    ) {
                        generateSnack(
                            view,
                            error.description.toString(),
                            dur = Snackbar.LENGTH_LONG
                        )
                        webviewError = true
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        if (!webviewError){
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
}
package com.example.rccamfrontend

import android.content.Intent
import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_connect.*

class ConnectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect)

        val ipPat = Regex("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])(\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])){3}\$")

        btnConnect.setOnClickListener{
            val ipInput = textfieldIP.text.toString()
            val portInput = textfieldPort.text.toString().toDoubleOrNull()

            val ipResult = ipPat.matches(ipInput)
            var nextActivity = true

            if(ipInput == ""){ // Checking if a value was entered for the IP
                generateToast(this, "Enter an IP Address")
                nextActivity = false

            }else if (!ipResult) { // Checking if IP address is valid
                generateToast( this,"Invalid IP Address")
                nextActivity = false
            }

            if(portInput == null){ // Checking if a value was entered for the port
                generateToast(this, "Enter a Port number")
                nextActivity = false

            } else if(portInput > 65536){ // Checking if port number is valid
                generateToast(this, "Invalid Port")
                nextActivity = false
            }

            if (nextActivity){ // Process for Main activity

                var webviewError = false

                val hiddenWebview = findViewById<WebView>(R.id.hiddenWebview)
                hiddenWebview.webViewClient = object : WebViewClient() { // Webpage failed to load
                    override fun onReceivedError (view: WebView, request: WebResourceRequest, error: WebResourceError) {
                        generateToast(this@ConnectActivity, error.description.toString(), dur=Toast.LENGTH_LONG)
                        webviewError = true
                    }

                    override fun onPageFinished(view: WebView?, url: String?) { // Go to next activity
                        super.onPageFinished(view, url)
                        generateToast(this@ConnectActivity, webviewError.toString())
                        if (!webviewError){
                            val intent = Intent(this@ConnectActivity, MainActivity::class.java)
                            intent.putExtra("ip", textfieldIP.text.toString())
                            intent.putExtra("port", textfieldPort.text.toString())

                            startActivity(intent)
                        }
                    }
                }

                hiddenWebview.loadUrl("http://%s:%s/".format(
                    textfieldIP.text.toString(),
                    textfieldPort.text.toString()))
            }
        }
    }
}
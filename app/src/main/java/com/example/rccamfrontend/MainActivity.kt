package com.example.rccamfrontend

import android.app.AlertDialog
import android.os.Bundle
import android.service.autofill.Validators.not
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view = findViewById<View>(R.id.mainConstraint)

        webview.webViewClient = object : WebViewClient() {
            override fun onReceivedError (view: WebView, request: WebResourceRequest, error: WebResourceError) {
                generateToast(this@MainActivity, error.description.toString(), dur=Toast.LENGTH_LONG)
            }
        }

        bottomNavigationBar.setOnNavigationItemSelectedListener{ item ->
            when(item.itemId) {
                R.id.action_face_detection -> {
                    generateSnack(view, "Face Detection")
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_shutter -> {
                    generateSnack(view, "Shutter")
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_rotation -> {
                    val dialog = AlertDialog.Builder(this)
                    val dialogView = this.layoutInflater.inflate(R.layout.dialogue_rotation, findViewById(R.id.content), false)

                    // Getting incrementation and decrementation buttons
                    val btnYawPlus = dialogView.findViewById<Button>(R.id.btnYawPlus)
                    val btnYawMinus = dialogView.findViewById<Button>(R.id.btnYawMinus)
                    val btnPitchPlus = dialogView.findViewById<Button>(R.id.btnPitchPlus)
                    val btnPitchMinus = dialogView.findViewById<Button>(R.id.btnPitchMinus)

                    // Getting textfields
                    val textfieldYaw = dialogView.findViewById<TextView>(R.id.textfieldYaw)
                    val textfieldPitch = dialogView.findViewById<TextView>(R.id.textfieldPitch)

                    btnYawPlus.setOnClickListener {  // Increment btnYaw
                        incrementTextView(textfieldYaw, 1)
                    }

                    btnYawMinus.setOnClickListener {  // Decrement btnYaw
                        incrementTextView(textfieldYaw, -1)
                    }

                    btnPitchPlus.setOnClickListener {  // Increment btnPitch
                        incrementTextView(textfieldPitch, 1)
                    }

                    btnPitchMinus.setOnClickListener{  // Decrement btnPitch
                        incrementTextView(textfieldPitch, -1)
                    }

                    dialog
                        .setTitle("Set Rotation")
                        .setView(dialogView)
                        .setPositiveButton("Confirm") {_, _ ->
                            val textfieldPitchData = textfieldPitch.text.toString()
                            val textfieldYawData = textfieldYaw.text.toString()

                            // <ip>/?p=int&y=int
                            // URL Arg Logic
                            var servoUrl = url
                            if (textfieldPitchData != "" || textfieldYawData != ""){
                                if (textfieldPitchData != ""){ // Pitch arg supplied
                                    servoUrl += "/?p=%s".format(textfieldPitchData)
                                    if (textfieldYawData != ""){ // Both args supplied
                                        servoUrl += "&y=%s".format(textfieldYawData)
                                    }
                                } else if (textfieldYawData != ""){ // Just yaw arg supplied
                                    servoUrl += "/?y=%s".format(textfieldYawData)
                                }

                                webview.loadUrl(servoUrl)
                            }
                        }
                        .setNegativeButton("Cancel") {_, _ ->
                            // Do nothing - Android auto dismisses
                        }
                        .show()
                    return@setOnNavigationItemSelectedListener true
                } else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }



        if (intent != null) {
            url = "http://%s:%s".format(
                intent.getStringExtra("ip"),
                intent.getStringExtra("port"))
            webview.loadUrl(url)

            generateSnack(view, "Loaded URL")
        }
    }

}


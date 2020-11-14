package com.example.rccamfrontend

import android.app.AlertDialog
import android.app.DownloadManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment.DIRECTORY_PICTURES
import android.view.View
import android.webkit.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view = findViewById<View>(R.id.mainConstraint)
        webview.setDownloadListener { url, _, contentDisposition, mimeType, _ ->
            // Getting filename
            val filename = URLUtil.guessFileName(url, contentDisposition, mimeType)

            // Setting up Download Request Manager
            val request = DownloadManager.Request(Uri.parse(url))
            request
                .setTitle("Photo")
                .setDescription("Taken from RPI")
                .setDestinationInExternalPublicDir(DIRECTORY_PICTURES, filename)

            // Setting up Main Download Manager
            val manager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)

        }

        // Error handling kinda
        webview.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                generateToast(
                    this@MainActivity,
                    error.description.toString(),
                    dur = Toast.LENGTH_LONG
                )
            }
        }

        bottomNavigationBar.setOnNavigationItemSelectedListener{ item ->
            when(item.itemId) {
                R.id.action_face_detection -> {
                    val faceURL = "$url?o=f"
                    webview.loadUrl(faceURL)

                    generateSnack(view, "Face Detection", anch = bottomNavigationBar)
                }
                R.id.action_shutter -> {
                    // Setting up Download Request Manager
                    val shutterURL = "$url/photo"
                    webview.loadUrl(shutterURL)

                    generateSnack(view, "Taken photo", anch = bottomNavigationBar)
                }
                R.id.action_rotation -> {
                    val dialog = AlertDialog.Builder(this)
                    val dialogView = this.layoutInflater.inflate(
                        R.layout.dialogue_rotation, findViewById(
                            R.id.content
                        ), false
                    )

                    // Getting textfields
                    val textfieldYaw = dialogView.findViewById<EditText>(R.id.textfieldYaw)
                    val textfieldPitch = dialogView.findViewById<EditText>(R.id.textfieldPitch)

                    // Getting incrementation and decrementation buttons
                    val btnYawPlus = dialogView.findViewById<Button>(R.id.btnYawPlus)
                    val btnYawMinus = dialogView.findViewById<Button>(R.id.btnYawMinus)
                    val btnPitchPlus = dialogView.findViewById<Button>(R.id.btnPitchPlus)
                    val btnPitchMinus = dialogView.findViewById<Button>(R.id.btnPitchMinus)


                    // Setting value limit for text views
                    textfieldPitch.doOnTextChanged { text, start, before, count ->
                        val textInt = text.toString().toIntOrNull()
                        if (textInt != null) {
                            if (textInt > 13) {
                                textfieldYaw.setText(getString(R.string.maxRot))
                            } else if (textInt < 0) {
                                textfieldYaw.setText("0")
                            }
                        }
                        textfieldPitch.setSelection(textfieldPitch.text.length)
                    }

                    textfieldYaw.doOnTextChanged { text, start, before, count ->
                        val textInt = text.toString().toIntOrNull()
                        if (textInt != null) {
                            if (textInt > 13) {
                                textfieldYaw.setText(getString(R.string.maxRot))
                            } else if (textInt < 0) {
                                textfieldYaw.setText("0")
                            }
                        }
                        textfieldYaw.setSelection(textfieldYaw.text.length)
                    }


                    // Setting onClick actions for inc/dec buttons
                    btnYawPlus.setOnClickListener {  // Increment btnYaw
                        incrementTextView(textfieldYaw, 1)
                    }

                    btnYawMinus.setOnClickListener {  // Decrement btnYaw
                        incrementTextView(textfieldYaw, -1)
                    }

                    btnPitchPlus.setOnClickListener {  // Increment btnPitch
                        incrementTextView(textfieldPitch, 1)
                    }

                    btnPitchMinus.setOnClickListener {  // Decrement btnPitch
                        incrementTextView(textfieldPitch, -1)
                    }

                    dialog
                        .setTitle("Set Rotation")
                        .setView(dialogView)
                        .setPositiveButton("Confirm") { _, _ ->
                            val textfieldPitchData = textfieldPitch.text.toString()
                            val textfieldYawData = textfieldYaw.text.toString()

                            // <ip>/servo?p=int&y=int
                            // URL Arg Logic
                            var servoUrl = ""

                            if (textfieldPitchData != "") { // Pitch arg supplied
                                servoUrl += "p=$textfieldPitchData&"
                            }

                            if (textfieldYawData != "") { // Yaw arg supplied
                                servoUrl += "y=$textfieldYawData"
                            }

                            if (servoUrl != "") {
                                webview.loadUrl("$url/servo?$servoUrl")
                            }

                        }
                        .setNegativeButton("Cancel") { _, _ ->
                            // Do nothing - Android auto dismisses
                        }
                        .show()
                }
            }
            return@setOnNavigationItemSelectedListener true
        }



        if (intent != null) {
            url = "http://%s:%s".format(
                intent.getStringExtra("ip"),
                intent.getStringExtra("port")
            )
            webview.loadUrl(url)

            generateSnack(view, "Loaded URL")
        }
    }

}


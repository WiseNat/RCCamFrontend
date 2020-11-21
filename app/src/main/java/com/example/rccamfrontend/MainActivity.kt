package com.example.rccamfrontend

import android.app.AlertDialog
import android.app.DownloadManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.view.Menu
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

        // Modifying Action Bar
        supportActionBar?.title = ""

        // Modifying Webview
        val view = findViewById<View>(R.id.mainConstraint)
        webview.setDownloadListener { thisUrl, _, contentDisposition, mimeType, _ ->
            // Getting filename and new URL
            val filename = URLUtil.guessFileName(thisUrl, contentDisposition, mimeType)
            val newUrl = "$url/get_photo/$filename"

            // Setting up Download Request Manager
            val request = DownloadManager.Request(Uri.parse(newUrl))
            request
                .setTitle(filename)
                .setDescription("Taken from RPI")
                .setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, filename)

            // Setting up Main Download Manager
            val manager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)

            val onDownloadComplete = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    val id = intent!!.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                    if (id != -1L) {
                        // File received
                        val uri = manager.getUriForDownloadedFile(id)
                        with(getPreferences(Context.MODE_PRIVATE).edit()){
                            putString("URI", uri.toString())
                            apply()
                        }
                    } else {
                        generateToast(this@MainActivity, "Failed to get ID, $id")
                    }
                }
            }

            registerReceiver(
                onDownloadComplete,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            )

            generateSnack(view, filename)

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

        bottomNavigationBar.findViewById<View>(R.id.action_shutter).setOnLongClickListener{ _ ->
            val dialog = AlertDialog.Builder(this)
            val dialogView = this.layoutInflater.inflate(
                R.layout.dialogue_timed,
                findViewById(R.id.content),
                false
            )

            // Getting textfield and buttons
            val textfieldTime = dialogView.findViewById<EditText>(R.id.textfieldTime)
            val btnTimePlus = dialogView.findViewById<Button>(R.id.btnTimePlus)
            val btnTimeMinus = dialogView.findViewById<Button>(R.id.btnTimeMinus)


            // Setting value limit for text views
            textfieldTime.doOnTextChanged { text, _, _, _ ->
                val textInt = text.toString().toIntOrNull()
                if (textInt != null && textInt < 0) {
                    textfieldTime.setText("0")
                }
                textfieldTime.setSelection(textfieldTime.text.length)
            }

            // Setting onClick actions for inc/dec buttons
            btnTimePlus.setOnClickListener {  // Increment btnYaw
                incrementTextView(textfieldTime, 1)
            }

            btnTimeMinus.setOnClickListener {  // Decrement btnYaw
                incrementTextView(textfieldTime, -1)
            }

            dialog
                .setTitle("Choose Time to Wait")
                .setView(dialogView)
                .setPositiveButton("Confirm") { _, _ ->
                    val textfieldTimeData = textfieldTime.text.toString()

                    // <ip>/take_photo/float
                    webview.loadUrl("$url/take_photo/$textfieldTimeData")

                }
                .setNegativeButton("Cancel") { _, _ ->
                    // Do nothing - Android auto dismisses
                }
                .show()
            return@setOnLongClickListener true
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
                    val shutterURL = "$url/take_photo"
                    webview.loadUrl(shutterURL)

                    // generateSnack(view, "Taken photo", anch = bottomNavigationBar)
                }
                R.id.action_rotation -> {
                    val dialog = AlertDialog.Builder(this)
                    val dialogView = this.layoutInflater.inflate(
                        R.layout.dialogue_rotation,
                        findViewById(R.id.content),
                        false
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
                    textfieldPitch.doOnTextChanged { text, _, _, _ ->
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

                    textfieldYaw.doOnTextChanged { text, _, _, _ ->
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }
}


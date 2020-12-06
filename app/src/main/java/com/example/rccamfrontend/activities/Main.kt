package com.example.rccamfrontend.activities

import android.app.AlertDialog
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.rccamfrontend.R
import com.example.rccamfrontend.utils.generateSnack
import com.example.rccamfrontend.utils.generateToast
import com.example.rccamfrontend.utils.incrementTextView
import kotlinx.android.synthetic.main.activity_connect.*
import kotlinx.android.synthetic.main.activity_main.*


class Main : AppCompatActivity() {
    // Address Values
    private lateinit var url: String
    private lateinit var ip: String
    private lateinit var port: String

    // View
    private lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Modifying Action Bar
        supportActionBar?.title = ""

        // Getting view
        view = findViewById(R.id.mainConstraint)

        // Setting Address values and changing URL
        if (intent != null) {
            ip = intent.getStringExtra("ip").toString()
            port = intent.getStringExtra("port").toString()

            url = "http://%s:%s".format(ip, port)
            webview.loadUrl(url)

            generateSnack(view, "Loaded URL: $ip")
        }

        // Setting Download Manager
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
                        generateToast(this@Main, "Failed to get ID, $id")
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
                    this@Main,
                    error.description.toString(),
                    dur = Toast.LENGTH_LONG
                )
            }
        }

        bottomNavigationBar.findViewById<View>(R.id.action_shutter).setOnLongClickListener{
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

                            // URL Arg Logic <ip>/servo?p=int&y=int
                            var servoUrl = ""

                            // Pitch arg supplied
                            if (textfieldPitchData != "") {
                                servoUrl += "p=$textfieldPitchData&"
                            }

                            // Yaw arg supplied
                            if (textfieldYawData != "") {
                                servoUrl += "y=$textfieldYawData"
                            }

                            // Yaw and/or Pitch args supplied
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            // Open Photo Gallery Clicked
            R.id.open_photo_gallery -> {
                val mediaUri = Uri.parse("content://media/internal/images/media")
                with(Intent(Intent.ACTION_VIEW, mediaUri)){
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(this)
                }
            }

            // Share Media Clicked
            R.id.share -> {
                with(Intent(Intent.ACTION_SEND)) {
                    val sharedPref = getPreferences(Context.MODE_PRIVATE)
                    val imagePath = sharedPref.getString("URI", "")

                    if (imagePath == "") {
                        generateSnack(
                            view,
                            "You haven't got a recent photo to share yet",
                            anch = bottomNavigationBar
                        )
                    } else {
                        type = "image/*"
                        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                        putExtra(Intent.EXTRA_STREAM, Uri.parse(imagePath))
                        startActivity(Intent.createChooser(this, "Share Image Using"))
                    }
                }
            }

            // About clicked
            R.id.about -> {
                with(Intent(this, About::class.java)) {
                    putExtra("ip", ip)
                    putExtra("port", port)
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}


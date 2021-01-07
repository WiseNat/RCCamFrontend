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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rccamfrontend.R
import com.example.rccamfrontend.utils.generateSnack
import com.example.rccamfrontend.utils.generateToast
import kotlinx.android.synthetic.main.activity_connect.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialogue_rotation.view.*
import kotlinx.android.synthetic.main.dialogue_timed.view.*


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
        view = mainConstraint

        // Setting Address values and changing URL
        if (intent != null) {
            ip = intent.getStringExtra("ip").toString()
            port = intent.getStringExtra("port").toString()

            url = "http://%s:%s".format(ip, port)
            webview.loadUrl(url)

            generateSnack(view, "Loaded URL: $ip", anch = bottomNavigationBar)
        }

        // Setting Download Manager
        webview.setDownloadListener { thisUrl, _, contentDisposition, mimeType, _ ->
            // Getting filename
            val filename = URLUtil.guessFileName(thisUrl, contentDisposition, mimeType)

            // Setting up Download Request Manager
            val request = DownloadManager.Request(Uri.parse("$url/get_photo/$filename"))
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

            generateSnack(view, filename, anch = bottomNavigationBar)

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

            dialog
                .setTitle("Choose Time to Wait")
                .setView(dialogView)
                .setPositiveButton("Confirm") { _, _ ->
                    val textfieldTimeData = dialogView.secIncTextView.textView.text.toString()

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
                }
                R.id.action_rotation -> {
                    val dialog = AlertDialog.Builder(this)
                    val dialogView = this.layoutInflater.inflate(
                        R.layout.dialogue_rotation,
                        findViewById(R.id.content),
                        false
                    )

                    dialog
                        .setTitle("Set Rotation")
                        .setView(dialogView)
                        .setPositiveButton("Confirm") { _, _ ->
                            val pitchVal = dialogView.yawIncTextView.textView.text.toString()
                            val yawVal = dialogView.pitchIncTextView.textView.text.toString()
                            webview.loadUrl("$url/servo?p=$pitchVal&y=$yawVal")
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

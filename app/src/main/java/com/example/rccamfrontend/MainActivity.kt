package com.example.rccamfrontend

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

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
                    generateSnack(view, "Rotation")
                    val alertBuilder = AlertDialog.Builder(this)
                    alertBuilder
                        .setTitle("Set Rotation")
                        .setView(this.layoutInflater.inflate(R.layout.dialogue_rotation, findViewById(R.id.content), false))
                        .setPositiveButton("Confirm") {_, _ ->
                            generateSnack(view, "Confirm code here")
                        }
                        .setNegativeButton("Cancel") {_, _ ->
                            // Do nothing - Android auto dismisses
                        }
                    alertBuilder.show()
                    return@setOnNavigationItemSelectedListener true
                } else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }



        if (intent != null) {
            webview.loadUrl("http://%s:%s/".format(
                intent.getStringExtra("ip"),
                intent.getStringExtra("port")))

            generateSnack(view, "Loaded URL")
        }
    }

}


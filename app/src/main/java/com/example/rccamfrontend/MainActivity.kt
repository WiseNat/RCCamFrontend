package com.example.rccamfrontend

import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webview = findViewById<WebView>(R.id.webview)
        webview.webViewClient = object : WebViewClient() {
            override fun onReceivedError (view: WebView, request: WebResourceRequest, error: WebResourceError) {
                generateToast(this@MainActivity, error.description.toString(), dur=Toast.LENGTH_LONG)
            }
        }

        if (intent != null) {
            webview.loadUrl("http://%s:%s/".format(
                intent.getStringExtra("ip"),
                intent.getStringExtra("port")))

            generateSnack(findViewById<View>(R.id.mainConstraint), "Loaded URL")
        }
    }

}


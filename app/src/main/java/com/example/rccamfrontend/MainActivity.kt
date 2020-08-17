package com.example.rccamfrontend

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (intent != null) {
            val webview = findViewById<WebView>(R.id.webView)
            webview.loadUrl("http://%s:%s/".format(
                intent.getStringExtra("ip"),
                intent.getStringExtra("port")))
        }


    }
}

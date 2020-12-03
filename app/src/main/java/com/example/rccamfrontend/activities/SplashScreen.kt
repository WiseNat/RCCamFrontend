package com.example.rccamfrontend.activities

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.Fade
import com.example.rccamfrontend.R

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, Connect::class.java)

            window.exitTransition = Fade()
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())

        }, 3000)
    }
}
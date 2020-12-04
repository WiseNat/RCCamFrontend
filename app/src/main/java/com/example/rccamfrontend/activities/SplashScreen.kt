package com.example.rccamfrontend.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.Fade
import androidx.appcompat.app.AppCompatActivity
import com.example.rccamfrontend.R

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Hiding the Action Bar
        supportActionBar?.hide()

        // Fade when going to the next activity
        window.exitTransition = Fade()

        // Runnable for "postDelayed"
        val r = Runnable {
            val intent = Intent(this, Connect::class.java)
            startActivity(
                intent,
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
            )
        }

        // Wait 3 seconds and then go to the "Connect" activity
        Handler(Looper.getMainLooper()).postDelayed(r, 3000)
    }
}
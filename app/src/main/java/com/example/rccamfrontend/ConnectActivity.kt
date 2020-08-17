package com.example.rccamfrontend

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_connect.*

class ConnectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect)

        val ipPat = Regex("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])(\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])){3}\$")

        btnConnect.setOnClickListener{
            val ipInput = textfieldIP.text.toString()
            val portInput = textfieldPort.text.toString().toDoubleOrNull()

            val ipResult = ipPat.matches(ipInput)
            var nextActivity = true

            if(ipInput == ""){ // Checking if a value was entered for the IP
                generateToast("Enter an IP Address")
                nextActivity = false

            }else if (!ipResult) { // Checking if IP address is valid
                generateToast("Invalid IP Address")
                nextActivity = false
            }

            if(portInput == null){ // Checking if a value was entered for the port
                generateToast("Enter a Port number")
                nextActivity = false

            } else if(portInput > 65536){ // Checking if port number is valid
                generateToast("Invalid Port")
                nextActivity = false

            }

            if (nextActivity){
                // Start Main activity
                val intent = Intent(this, MainActivity::class.java)

                intent.putExtra("ip", textfieldIP.text.toString())
                intent.putExtra("port", textfieldPort.text.toString())

                startActivity(intent)
            }
        }
    }

    private fun generateToast(text: String, duration: Int = Toast.LENGTH_SHORT){
        Toast.makeText(this, text, duration).show()
    }

}
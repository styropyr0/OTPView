package com.matrix.testproject

import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.matrix.otpview.OtpView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val otpView = findViewById<OtpView>(R.id.otp)
        otpView.apply {
            setInputType(InputType.TYPE_CLASS_TEXT)
            setSquareCount(6)
            setOnCompleteListener { it ->
                if (!checkOTP(it))
                    onOtpError(true)
            }
        }
    }

    fun checkOTP(otp: String): Boolean {
        return otp == "123456"
    }

}
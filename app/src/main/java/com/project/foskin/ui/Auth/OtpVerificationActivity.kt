package com.project.foskin.ui.Auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.project.foskin.R
import com.project.foskin.databinding.ActivityOtpVerificationBinding

class OtpVerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpVerificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupOtpBoxes()
        setupResendButton()
        setupBackButton()
        setupVerifyButton()

        binding.verifyButton.background = getDrawable(R.drawable.rounded_button_otp)
        binding.verifyButton.backgroundTintList = null
    }

    private fun setupOtpBoxes() {
        val otpBoxes = listOf<EditText>(
            findViewById(R.id.otpBox1),
            findViewById(R.id.otpBox2),
            findViewById(R.id.otpBox3),
            findViewById(R.id.otpBox4),
            findViewById(R.id.otpBox5),
            findViewById(R.id.otpBox6)
        )
        val errorMessage = findViewById<TextView>(R.id.otpErrorMessage)

        for (i in otpBoxes.indices) {
            otpBoxes[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1 && i < otpBoxes.size - 1) {
                        otpBoxes[i + 1].requestFocus()
                    } else if (s?.isEmpty() == true && i > 0) {
                        otpBoxes[i - 1].requestFocus()
                    }

                    if (errorMessage.visibility == View.VISIBLE) {
                        errorMessage.visibility = View.GONE
                    }

                    otpBoxes[i].background = getDrawable(R.drawable.otp_box_background)
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun setupResendButton() {
        val resendText = findViewById<TextView>(R.id.resendCodeText)
        val fullText = "Don't receive a code? Resend"
        val spannableString = android.text.SpannableString(fullText)

        val resendStart = fullText.indexOf("Resend")
        val resendEnd = resendStart + "Resend".length

        spannableString.setSpan(
            android.text.style.ForegroundColorSpan(android.graphics.Color.parseColor("#00008B")),
            resendStart,
            resendEnd,
            android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        resendText.text = spannableString

        resendText.setOnClickListener {
            Toast.makeText(this, "Verification code resent!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBackButton() {
        val backButton = findViewById<ImageView>(R.id.backButtonOtp)
        backButton.setOnClickListener {
            val intent = Intent(this, EnterWhatsappNumberActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupVerifyButton() {
        val verifyButton = findViewById<TextView>(R.id.verifyButton)
        val errorMessage = findViewById<TextView>(R.id.otpErrorMessage)
        val otpBoxes = listOf<EditText>(
            findViewById(R.id.otpBox1),
            findViewById(R.id.otpBox2),
            findViewById(R.id.otpBox3),
            findViewById(R.id.otpBox4),
            findViewById(R.id.otpBox5),
            findViewById(R.id.otpBox6)
        )

        verifyButton.setOnClickListener {
            var allFilled = true

            for (otpBox in otpBoxes) {
                if (otpBox.text.toString().isEmpty()) {
                    allFilled = false
                    otpBox.background = getDrawable(R.drawable.error_background)
                } else {
                    otpBox.background = getDrawable(R.drawable.otp_box_background)
                }
            }

            if (!allFilled) {
                errorMessage.text = "All fields must be filled"
                errorMessage.visibility = View.VISIBLE
            } else {
                val otpInput = otpBoxes.joinToString("") { it.text.toString() }

                if (otpInput != "123456") {
                    errorMessage.text = "Invalid OTP"
                    errorMessage.visibility = View.VISIBLE

                    otpBoxes.forEach { otpBox ->
                        otpBox.text.clear()
                        otpBox.background = getDrawable(R.drawable.otp_box_background)
                    }

                    otpBoxes[0].requestFocus()

                    Toast.makeText(this, "Incorrect OTP. Please try again", Toast.LENGTH_SHORT).show()
                } else {
                    errorMessage.visibility = View.GONE
                    Toast.makeText(this, "OTP Verified!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, QuickSurvey1Activity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}

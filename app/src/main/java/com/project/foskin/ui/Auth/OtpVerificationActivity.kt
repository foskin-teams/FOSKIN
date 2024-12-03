package com.project.foskin.ui.Auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.project.foskin.R

class OtpVerificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)
        supportActionBar?.hide()

        setupOtpBoxes()
        setupResendButton()
        setupBackButton()
        setupVerifyButton()
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

                    // Hide error message and reset field if user starts typing
                    if (errorMessage.visibility == View.VISIBLE) {
                        errorMessage.visibility = View.GONE
                    }

                    // Reset background color to normal
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

        // Set click listener for Resend
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

            // Periksa apakah semua kolom diisi
            for (otpBox in otpBoxes) {
                if (otpBox.text.toString().isEmpty()) {
                    allFilled = false
                    otpBox.background = getDrawable(R.drawable.error_background) // Menyoroti kolom kosong
                } else {
                    otpBox.background = getDrawable(R.drawable.otp_box_background) // Kembalikan background ke normal
                }
            }

            // Menampilkan pesan error jika ada kolom yang kosong
            if (!allFilled) {
                errorMessage.text = "All fields must be filled"
                errorMessage.visibility = View.VISIBLE
            } else {
                // Cek OTP, ganti dengan OTP yang valid sesuai kebutuhan Anda
                val otpInput = otpBoxes.joinToString("") { it.text.toString() }

                if (otpInput != "123456") { // Ganti dengan OTP yang valid
                    // Jika OTP salah
                    errorMessage.text = "Invalid OTP"
                    errorMessage.visibility = View.VISIBLE

                    // Setelah error, reset kolom
                    otpBoxes.forEach { otpBox ->
                        otpBox.text.clear() // Hapus teks pada semua kolom
                        otpBox.background = getDrawable(R.drawable.otp_box_background) // Kembalikan background ke normal
                    }

                    // Fokus ke kolom pertama
                    otpBoxes[0].requestFocus()

                    // Tampilkan pesan kesalahan
                    Toast.makeText(this, "Incorrect OTP. Please try again", Toast.LENGTH_SHORT).show()
                } else {
                    // Jika OTP benar
                    errorMessage.visibility = View.GONE
                    Toast.makeText(this, "OTP Verified!", Toast.LENGTH_SHORT).show()

                    // Intent ke QuickSurveyActivity setelah OTP verifikasi berhasil
                    val intent = Intent(this, QuickSurveyActivity::class.java)
                    startActivity(intent)
                    finish()  // Optional: If you want to close the current activity
                }
            }
        }
    }
}

package com.project.foskin.ui.Auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.foskin.R

class EnterWhatsappNumberActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_whatsapp_number)
        supportActionBar?.hide()

        setupTermsAndPolicyText()
        setupPhoneNumberInput()
    }

    private fun setupTermsAndPolicyText() {
        val textView = findViewById<TextView>(R.id.termsAndPolicyText)
        val fullText = "By signing up, you agree to our Terms of Service and Privacy Policy"
        val spannableString = android.text.SpannableString(fullText)

        val termsStart = fullText.indexOf("Terms of Service")
        val termsEnd = termsStart + "Terms of Service".length

        val privacyStart = fullText.indexOf("Privacy Policy")
        val privacyEnd = privacyStart + "Privacy Policy".length

        spannableString.setSpan(
            android.text.style.ForegroundColorSpan(Color.parseColor("#00008B")),
            termsStart,
            termsEnd,
            android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            android.text.style.ForegroundColorSpan(Color.parseColor("#00008B")),
            privacyStart,
            privacyEnd,
            android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textView.text = spannableString
    }

    private fun setupPhoneNumberInput() {
        val phoneNumberInput = findViewById<EditText>(R.id.phoneNumberInput)
        val continueButton = findViewById<Button>(R.id.continueButton)
        val errorMessageText = findViewById<TextView>(R.id.errorMessageText)

        // Membatasi panjang input menjadi 13 digit
        phoneNumberInput.filters = arrayOf(android.text.InputFilter.LengthFilter(13))

        phoneNumberInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isNotEmpty = s?.isNotEmpty() == true

                if (isNotEmpty) {
                    // Enable the button and change its background
                    continueButton.isEnabled = true
                    continueButton.background = getDrawable(R.drawable.rounded_button_enabled)
                } else {
                    // Disable the button and reset its background
                    continueButton.isEnabled = false
                    continueButton.background = getDrawable(R.drawable.rounded_button_disabled)
                }

                // Reset the error message visibility when typing
                errorMessageText.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        continueButton.setOnClickListener {
            val phoneNumber = phoneNumberInput.text.toString()
            if (phoneNumber.startsWith("0")) {
                // Show error message if the number starts with "0"
                errorMessageText.text = "Please do not start the number with 0"
                errorMessageText.visibility = View.VISIBLE
            } else if (phoneNumber.length < 10) {
                // Error message if the number is less than 10 digits
                errorMessageText.text = "Phone number must be at least 10 digits"
                errorMessageText.visibility = View.VISIBLE
            } else {
                // Navigate to OtpVerificationActivity
                val intent = Intent(this, OtpVerificationActivity::class.java)
                intent.putExtra("PHONE_NUMBER", phoneNumber) // Pass phone number to the next activity
                startActivity(intent)
            }
        }
    }
}
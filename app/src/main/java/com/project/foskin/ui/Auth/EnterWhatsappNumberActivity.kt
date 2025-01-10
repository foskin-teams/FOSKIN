package com.project.foskin.ui.Auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hbb20.CountryCodePicker
import com.project.foskin.R
import com.project.foskin.data.remote.api.ApiConfig
import com.project.foskin.data.remote.api.SendOtpRequest
import com.project.foskin.data.remote.api.SendOtpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        val countryCodePicker = findViewById<CountryCodePicker>(R.id.ccp)

        phoneNumberInput.filters = arrayOf(android.text.InputFilter.LengthFilter(12))

        phoneNumberInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isNotEmpty = s?.isNotEmpty() == true

                if (isNotEmpty) {
                    continueButton.isEnabled = true
                    continueButton.background = getDrawable(R.drawable.rounded_button_enabled)
                    continueButton.backgroundTintList = null
                } else {
                    continueButton.isEnabled = false
                    continueButton.background = getDrawable(R.drawable.rounded_button_disabled)
                    continueButton.backgroundTintList = null
                }

                errorMessageText.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        continueButton.setOnClickListener {
            val phoneNumber = phoneNumberInput.text.toString()
            val countryCode = countryCodePicker.selectedCountryCodeWithPlus

            if (phoneNumber.startsWith("0")) {
                errorMessageText.text = "Please do not start the number with 0"
                errorMessageText.visibility = View.VISIBLE
            } else if (phoneNumber.length < 10) {
                errorMessageText.text = "Phone number must be at least 10 digits"
                errorMessageText.visibility = View.VISIBLE
            } else {
                val fullPhoneNumber = "$countryCode$phoneNumber"
                sendOtp(fullPhoneNumber)
            }
        }
    }

    private fun sendOtp(phoneNumber: String) {
        val apiService = ApiConfig.getApiService()
        val request = SendOtpRequest(phoneNumber)
        val dummyOtp = "123456" // Dummy OTP

        apiService.sendOtp(request).enqueue(object : Callback<SendOtpResponse> {
            override fun onResponse(call: Call<SendOtpResponse>, response: Response<SendOtpResponse>) {
                if (response.isSuccessful) {
                    val otpNumber = response.body()?.data?.otpNumber ?: dummyOtp
                    Log.d("SendOtp", "OTP Received: $otpNumber")

                    val intent = Intent(this@EnterWhatsappNumberActivity, OtpVerificationActivity::class.java)
                    intent.putExtra("PHONE_NUMBER", phoneNumber)
                    intent.putExtra("DUMMY_OTP", otpNumber) // Kirim OTP ke Activity berikutnya
                    startActivity(intent)
                } else {
                    fallbackToDummyOtp(phoneNumber, dummyOtp)
                }
            }

            override fun onFailure(call: Call<SendOtpResponse>, t: Throwable) {
                fallbackToDummyOtp(phoneNumber, dummyOtp)
            }
        })
    }

    private fun fallbackToDummyOtp(phoneNumber: String, dummyOtp: String) {
        Log.e("SendOtp", "Using Dummy OTP: $dummyOtp")

        val intent = Intent(this@EnterWhatsappNumberActivity, OtpVerificationActivity::class.java)
        intent.putExtra("PHONE_NUMBER", phoneNumber)
        intent.putExtra("DUMMY_OTP", dummyOtp) // Kirim OTP ke Activity berikutnya
        startActivity(intent)
    }


}

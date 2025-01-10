package com.project.foskin.ui.Auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.foskin.R
import com.project.foskin.data.remote.api.ApiConfig
import com.project.foskin.data.remote.api.SendOtpRequest
import com.project.foskin.data.remote.api.SendOtpResponse
import com.project.foskin.data.remote.api.SignInRequest
import com.project.foskin.data.remote.api.SignInResponse
import com.project.foskin.databinding.ActivityOtpVerificationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        val otpBoxes = listOf(
            binding.otpBox1,
            binding.otpBox2,
            binding.otpBox3,
            binding.otpBox4,
            binding.otpBox5,
            binding.otpBox6
        )
        val errorMessage = binding.otpErrorMessage

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
        val resendText = binding.resendCodeText
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
            val phoneNumber = intent.getStringExtra("PHONE_NUMBER") ?: ""
            if (phoneNumber.isEmpty()) {
                Toast.makeText(this, "Phone number is missing", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Reset OTP fields before sending new OTP
            val otpBoxes = listOf(
                binding.otpBox1,
                binding.otpBox2,
                binding.otpBox3,
                binding.otpBox4,
                binding.otpBox5,
                binding.otpBox6
            )
            otpBoxes.forEach { it.text.clear() }

            sendOtp(phoneNumber)
        }
    }

    private fun setupBackButton() {
        val backButton = binding.backButtonOtp
        backButton.setOnClickListener {
            val intent = Intent(this, EnterWhatsappNumberActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupVerifyButton() {
        val verifyButton = binding.verifyButton
        val errorMessage = binding.otpErrorMessage
        val otpBoxes = listOf(
            binding.otpBox1,
            binding.otpBox2,
            binding.otpBox3,
            binding.otpBox4,
            binding.otpBox5,
            binding.otpBox6
        )

        verifyButton.setOnClickListener {
            val otpInput = otpBoxes.joinToString(separator = "") { it.text.toString() }
            if (otpInput.length < 6) {
                errorMessage.text = "All fields must be filled"
                errorMessage.visibility = View.VISIBLE
                otpBoxes.forEach { it.background = getDrawable(R.drawable.error_background) }
                return@setOnClickListener
            }

            val phoneNumber = intent.getStringExtra("PHONE_NUMBER") ?: ""
            val dummyOtp = intent.getStringExtra("DUMMY_OTP") ?: ""

            if (otpInput == dummyOtp) {
                // Jika OTP sesuai dengan dummy OTP
                Toast.makeText(this, "Dummy OTP Verification Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, QuickSurvey1Activity::class.java)
                startActivity(intent)
                finish()
            } else {
                verifyOtp(phoneNumber, otpInput)
            }
        }
    }

    private fun sendOtp(phoneNumber: String) {
        val apiService = ApiConfig.getApiService()
        val request = SendOtpRequest(phoneNumber)

        apiService.sendOtp(request).enqueue(object : Callback<SendOtpResponse> {
            override fun onResponse(call: Call<SendOtpResponse>, response: Response<SendOtpResponse>) {
                if (response.isSuccessful) {
                    val otpNumber = response.body()?.data?.otpNumber
                    if (!otpNumber.isNullOrEmpty()) {
                        // Log OTP to logcat
                        Log.d("SendOtp", "OTP Received: $otpNumber")

                        val intent = Intent(this@OtpVerificationActivity, OtpVerificationActivity::class.java)
                        intent.putExtra("PHONE_NUMBER", phoneNumber)
                        startActivity(intent)
                    } else {
                        Log.d("SendOtp", "OTP data is empty")
                        Toast.makeText(this@OtpVerificationActivity, "Failed to send OTP", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d("SendOtp", "Failed to send OTP: ${response.message()}")
                    Toast.makeText(this@OtpVerificationActivity, "Failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SendOtpResponse>, t: Throwable) {
                Log.e("SendOtp", "Error: ${t.message}", t)
                Toast.makeText(this@OtpVerificationActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun verifyOtp(phoneNumber: String, otp: String) {
        val apiService = ApiConfig.getApiService()
        val request = SignInRequest(phoneNumber, otp)

        apiService.signIn(request).enqueue(object : Callback<SignInResponse> {
            override fun onResponse(call: Call<SignInResponse>, response: Response<SignInResponse>) {
                Log.d("DEBUG", "Response code: ${response.code()}")
                Log.d("DEBUG", "Response message: ${response.message()}")
                Log.d("DEBUG", "Response body: ${response.body()?.toString()}")

                if (response.isSuccessful) {
                    val token = response.body()?.data?.accessToken
                    if (!token.isNullOrEmpty()) {
                        // Success - redirect to the next activity
                        Toast.makeText(this@OtpVerificationActivity, "Login successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@OtpVerificationActivity, QuickSurvey1Activity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        showError("The OTP code entered is incorrect")
                    }
                } else {
                    // Handle the error case
                    val errorDetail = response.errorBody()?.string() ?: "No additional details"
                    // Check for specific error code or message from the API
                    if (response.code() == 400 && errorDetail.contains("Invalid OTP", true)) {
                        showError("The OTP code entered is incorrect")
                    } else {
                        showError("Failed: $errorDetail")
                    }
                    Log.e("DEBUG", "Error detail: $errorDetail")
                }
            }

            override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                showError("Error: ${t.message}")
            }
        })
    }

    private fun showError(message: String) {
        val errorMessage = binding.otpErrorMessage
        errorMessage.text = message
        errorMessage.visibility = View.VISIBLE

        val otpBoxes = listOf(
            binding.otpBox1,
            binding.otpBox2,
            binding.otpBox3,
            binding.otpBox4,
            binding.otpBox5,
            binding.otpBox6
        )

        otpBoxes.forEach {
            it.background = getDrawable(R.drawable.error_background)
        }
    }
}

package com.project.foskin.ui.Auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.foskin.R
import com.project.foskin.databinding.ActivityQuickSurvey1Binding

class QuickSurvey1Activity : AppCompatActivity() {

    private lateinit var binding: ActivityQuickSurvey1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuickSurvey1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Set click listener for the Next button
        binding.btnNext.setOnClickListener {
            if (validateInputs()) {
                val intent = Intent(this, QuickSurvey2Activity::class.java)
                startActivity(intent)
            }
        }

        // Set click listener for icon_minus
        binding.iconMinus.setOnClickListener {
            val currentAge = binding.etAgeText.text.toString().toIntOrNull() ?: 0
            if (currentAge > 0) {
                binding.etAgeText.setText((currentAge - 1).toString())
            }
        }

        // Set click listener for icon_plus
        binding.iconPlus.setOnClickListener {
            val currentAge = binding.etAgeText.text.toString().toIntOrNull() ?: 0
            if (currentAge < 120) { // Maximum age limit
                binding.etAgeText.setText((currentAge + 1).toString())
            }
        }

        // Set click listener for buttonMan
        binding.buttonMan.setOnClickListener {
            selectGender(true)
        }

        // Set click listener for buttonWoman
        binding.buttonWoman.setOnClickListener {
            selectGender(false)
        }
    }

    private fun validateInputs(): Boolean {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val ageText = binding.etAgeText.text.toString().trim()
        val isGenderSelected = binding.buttonMan.isSelected || binding.buttonWoman.isSelected

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || !email.endsWith("@gmail.com")) {
            Toast.makeText(this, "Please enter a valid Gmail address", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(ageText) || ageText.toIntOrNull() == null || ageText.toInt() <= 0 || ageText.toInt() > 120) {
            Toast.makeText(this, "Please enter a valid age (1-120)", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!isGenderSelected) {
            Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun selectGender(isMan: Boolean) {
        if (isMan) {
            binding.buttonMan.setBackgroundResource(R.drawable.shape_quick)
            binding.buttonWoman.setBackgroundResource(R.drawable.shape)
            binding.buttonMan.isSelected = true
            binding.buttonWoman.isSelected = false
        } else {
            binding.buttonWoman.setBackgroundResource(R.drawable.shape_quick)
            binding.buttonMan.setBackgroundResource(R.drawable.shape)
            binding.buttonWoman.isSelected = true
            binding.buttonMan.isSelected = false
        }
    }
}

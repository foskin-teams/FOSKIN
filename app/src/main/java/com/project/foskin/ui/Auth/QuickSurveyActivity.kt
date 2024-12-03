package com.project.foskin.ui.Auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.foskin.MainActivity
import com.project.foskin.R

class QuickSurveyActivity : AppCompatActivity() {

    private var selectedGender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_quick_survey)

        // Gender Buttons
        val buttonMan = findViewById<LinearLayout>(R.id.button_man)
        val buttonWoman = findViewById<LinearLayout>(R.id.button_woman)

        // Age Increment & Decrement Buttons
        val iconMinus = findViewById<ImageView>(R.id.icon_minus)
        val iconPlus = findViewById<ImageView>(R.id.icon_plus)
        val ageEditText = findViewById<EditText>(R.id.age_text)

        // Name and Email EditTexts
        val nameEditText = findViewById<EditText>(R.id.et_name)
        val emailEditText = findViewById<EditText>(R.id.et_email)

        var number = 0
        ageEditText.setText(number.toString())

        // Handle Gender Selection
        buttonMan.setOnClickListener {
            selectedGender = "Man"
            buttonMan.setBackgroundResource(R.drawable.button_selected)
            buttonWoman.setBackgroundResource(R.drawable.shape)
        }

        buttonWoman.setOnClickListener {
            selectedGender = "Woman"
            buttonWoman.setBackgroundResource(R.drawable.button_selected)
            buttonMan.setBackgroundResource(R.drawable.shape)
        }

        // Handle Age Increment
        iconMinus.setOnClickListener {
            if (number > 0) {
                number--
                ageEditText.setText(number.toString())
            }
        }

        // Handle Age Decrement
        iconPlus.setOnClickListener {
            number++
            ageEditText.setText(number.toString())
        }

        // Ensure Valid Input in Age EditText
        ageEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val input = ageEditText.text.toString().toIntOrNull()
                number = input ?: 0
                ageEditText.setText(number.toString())
            }
        }

        // Finish Button Logic
        val btnFinish = findViewById<Button>(R.id.btn_finish)
        btnFinish.setOnClickListener {
            // Validasi Nama, Email, Usia dan Gender
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val age = ageEditText.text.toString().trim()

            // Validasi Nama
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your name.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi Email
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi Usia
            if (age.isEmpty() || age == "0") {
                Toast.makeText(this, "Please enter your age.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi Gender
            if (selectedGender == null) {
                Toast.makeText(this, "Please select your gender.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Jika semua validasi berhasil
            Toast.makeText(this, "Selected Gender: $selectedGender", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
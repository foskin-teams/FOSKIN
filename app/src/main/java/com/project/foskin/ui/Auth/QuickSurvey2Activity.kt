package com.project.foskin.ui.Auth

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.foskin.MainActivity
import com.project.foskin.R
import com.project.foskin.databinding.ActivityQuickSurvey2Binding

class QuickSurvey2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityQuickSurvey2Binding
    private var selectedSurveyOption: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuickSurvey2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.survey1.setOnClickListener { selectSurveyOption(binding.survey1) }
        binding.survey2.setOnClickListener { selectSurveyOption(binding.survey2) }
        binding.survey3.setOnClickListener { selectSurveyOption(binding.survey3) }
        binding.survey4.setOnClickListener { selectSurveyOption(binding.survey4) }

        binding.btnPrevious.setOnClickListener {
            navigateToQuickSurvey1()
        }

        binding.btnFinish.setOnClickListener {
            validateAndNavigateToMain()
        }
    }

    private fun selectSurveyOption(selectedOption: FrameLayout) {
        binding.survey1.setBackgroundResource(R.drawable.shape)
        binding.survey2.setBackgroundResource(R.drawable.shape)
        binding.survey3.setBackgroundResource(R.drawable.shape)
        binding.survey4.setBackgroundResource(R.drawable.shape)

        selectedOption.setBackgroundResource(R.drawable.shape_quick)
        selectedSurveyOption = selectedOption
    }

    private fun navigateToQuickSurvey1() {
        val intent = Intent(this, QuickSurvey1Activity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateAndNavigateToMain() {
        if (selectedSurveyOption == null) {
            Toast.makeText(this, "Please select one option before proceeding.", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

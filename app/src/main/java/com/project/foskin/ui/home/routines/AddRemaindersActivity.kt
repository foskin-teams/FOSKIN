package com.project.foskin.ui.home.routines

import android.os.Bundle
import android.widget.ImageButton
import android.widget.NumberPicker
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.project.foskin.R

class AddRemaindersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_remainders)
        supportActionBar?.hide()

        val npHour: NumberPicker = findViewById(R.id.np_hour)
        val npMinute: NumberPicker = findViewById(R.id.np_minute)

        npHour.minValue = 0
        npHour.maxValue = 23
        npHour.wrapSelectorWheel = true

        npMinute.minValue = 0
        npMinute.maxValue = 59
        npMinute.wrapSelectorWheel = true

        val btnBack: ImageButton = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }
    }
}

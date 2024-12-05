package com.project.foskin.ui.home.routines

import android.os.Bundle
import android.widget.ImageButton
import android.widget.NumberPicker
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.project.foskin.R
import java.util.*

class AddRemaindersActivity : AppCompatActivity() {

    private lateinit var npHour: NumberPicker
    private lateinit var npMinute: NumberPicker
    private lateinit var tvSubheader: TextView
    private lateinit var tvRepeat: TextView
    private lateinit var tvAlarmPeriod: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_remainders)
        supportActionBar?.hide()

        // Initialize components
        npHour = findViewById(R.id.np_hour)
        npMinute = findViewById(R.id.np_minute)
        tvSubheader = findViewById(R.id.tv_subheader)
        tvRepeat = findViewById(R.id.tv_repeat)
        tvAlarmPeriod = findViewById(R.id.tv_alarm_period) // New

        // Set limits for NumberPicker
        npHour.minValue = 0
        npHour.maxValue = 23
        npHour.wrapSelectorWheel = true

        npMinute.minValue = 0
        npMinute.maxValue = 59
        npMinute.wrapSelectorWheel = true

        // Initialize current time display
        updateSubheaderTime()
        updateAlarmPeriod() // Initialize period display

        // Add listener for NumberPicker changes
        npHour.setOnValueChangedListener { _, _, _ ->
            updateSubheaderTime()
            updateAlarmPeriod() // Update period when hour changes
        }
        npMinute.setOnValueChangedListener { _, _, _ -> updateSubheaderTime() }

        // Set listener for tvRepeat click to show dialog
        tvRepeat.setOnClickListener {
            showRepeatOptionsDialog()
        }

        // Back button action
        val btnBack: ImageButton = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun updateSubheaderTime() {
        val calendarNow = Calendar.getInstance()

        val currentTimeInMinutes = calendarNow.get(Calendar.HOUR_OF_DAY) * 60 + calendarNow.get(Calendar.MINUTE)

        val totalMinutesSelected = npHour.value * 60 + npMinute.value

        val remainingTimeInMinutes = totalMinutesSelected - currentTimeInMinutes

        if (remainingTimeInMinutes > 0) {
            val remainingHours = remainingTimeInMinutes / 60
            val remainingMinutes = remainingTimeInMinutes % 60
            tvSubheader.text = "Alarm in $remainingHours hour $remainingMinutes minute"
        } else {
            tvSubheader.text = "Alarm time has passed"
        }
    }

    private fun updateAlarmPeriod() {
        val hour = npHour.value
        tvAlarmPeriod.text = if (hour in 0..11) "Morning" else "Evening"
    }

    private fun showRepeatOptionsDialog() {
        val repeatOptions = arrayOf("Once", "Daily")
        val currentSelection = repeatOptions.indexOf(tvRepeat.text.toString())

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Repeat Options")
        builder.setSingleChoiceItems(repeatOptions, currentSelection) { dialog, which ->
            tvRepeat.text = repeatOptions[which] // Update the text in tvRepeat
            dialog.dismiss() // Close the dialog
        }
        builder.create().show()
    }
}

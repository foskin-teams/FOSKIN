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
    private lateinit var tvSkincareItem: TextView
    private lateinit var tvAlarmPeriod: TextView
    private lateinit var tvChooseSkincareItem: TextView

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
        tvAlarmPeriod = findViewById(R.id.tv_alarm_period)
        tvSkincareItem = findViewById(R.id.tv_skincare_items)
        tvChooseSkincareItem = findViewById(R.id.tvChooseSkincareItem)

        npHour.minValue = 0
        npHour.maxValue = 23
        npHour.wrapSelectorWheel = true

        npMinute.minValue = 0
        npMinute.maxValue = 59
        npMinute.wrapSelectorWheel = true

        updateSubheaderTime()
        updateAlarmPeriod()

        npHour.setOnValueChangedListener { _, _, _ ->
            updateSubheaderTime()
            updateAlarmPeriod()
        }
        npMinute.setOnValueChangedListener { _, _, _ -> updateSubheaderTime() }

        tvRepeat.setOnClickListener {
            showRepeatOptionsDialog()
        }

        tvSkincareItem.setOnClickListener {
            showItemSkincareOptionsDialog()
        }

        val btnBack: ImageButton = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun showItemSkincareOptionsDialog() {
        val skincareOptions = arrayOf(
            "Micellar Water", "Facial Wash", "Exfoliating", "Toner", "Serum",
            "Retinol", "Sunscreen", "Moisturizing", "Night Cream", "Eye Cream",
            "Sleeping Mask", "Essence"
        )

        val selectedItems = BooleanArray(skincareOptions.size)
        val selectedNames = mutableListOf<String>()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose Skincare Items")

        builder.setMultiChoiceItems(skincareOptions, selectedItems) { _, which, isChecked ->
            if (isChecked) {
                selectedNames.add(skincareOptions[which])
            } else {
                selectedNames.remove(skincareOptions[which])
            }
        }

        builder.setPositiveButton("OK") { _, _ ->
            val selectedText = selectedNames.joinToString(", ")
            findViewById<TextView>(R.id.tvChooseSkincareItem).text = selectedText
        }

        builder.setNegativeButton("Cancel", null)

        builder.create().show()
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
            tvRepeat.text = repeatOptions[which]
            dialog.dismiss()
        }
        builder.create().show()
    }
}

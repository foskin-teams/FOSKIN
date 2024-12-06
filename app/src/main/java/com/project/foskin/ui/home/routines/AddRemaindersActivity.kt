package com.project.foskin.ui.home.routines

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.NumberPicker
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
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
    private lateinit var btnBack: ImageButton
    private lateinit var btnSave: ImageButton
    private lateinit var alarmViewModel: AlarmViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_remainders)
        supportActionBar?.hide()

        npHour = findViewById(R.id.np_hour)
        npMinute = findViewById(R.id.np_minute)
        tvSubheader = findViewById(R.id.tv_subheader)
        tvRepeat = findViewById(R.id.tv_repeat)
        tvAlarmPeriod = findViewById(R.id.tv_alarm_period)
        tvSkincareItem = findViewById(R.id.tv_skincare_items)
        tvChooseSkincareItem = findViewById(R.id.tvChooseSkincareItem)
        btnBack = findViewById(R.id.btn_back)
        btnSave = findViewById(R.id.btn_save)
        alarmViewModel = ViewModelProvider(this)[AlarmViewModel::class.java]


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
        npMinute.setOnValueChangedListener { _, _, _ ->
            updateSubheaderTime()
        }

        tvRepeat.setOnClickListener {
            showRepeatOptionsDialog()
        }

        tvSkincareItem.setOnClickListener {
            showItemSkincareOptionsDialog()
        }

        btnBack.setOnClickListener {
            finish()
        }

        btnSave.setOnClickListener {
            if (validateInputs()) {
                val hour = npHour.value
                val minute = npMinute.value
                val period = tvAlarmPeriod.text.toString()
                val repeat = tvRepeat.text.toString()
                val skincareItems = tvChooseSkincareItem.text.toString()
                val vibrate = findViewById<Switch>(R.id.switch_vibrate).isChecked
                val deleteAfterRing = findViewById<Switch>(R.id.switch_delete_after_ringing).isChecked

                val newAlarm = AlarmData(
                    id = System.currentTimeMillis(),
                    hour = hour,
                    minute = minute,
                    period = period,
                    repeat = repeat,
                    skincareItems = skincareItems,
                    vibrate = vibrate,
                    deleteAfterRinging = deleteAfterRing
                )

                alarmViewModel.addAlarm(this, newAlarm)

                // Load alarms immediately
                alarmViewModel.loadAlarms(this)

                Toast.makeText(this, "Alarm saved successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun setAlarm(hour: Int, minute: Int, message: String) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        val alarmIntent = Intent(this, AlarmReceiver::class.java).apply {
            putExtra("alarm_message", message)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
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

    private fun showItemSkincareOptionsDialog() {
        val skincareOptions = arrayOf(
            "Micellar Water", "Facial Wash", "Exfoliating", "Toner", "Essence", "Serum",
            "Retinol", "Moisturizing", "Eye Cream", "Night Cream", "Sleeping Mask", "Sunscreen"
        )

        val selectedItems = mutableListOf<String>()
        val currentItemsText = tvChooseSkincareItem.text.toString()
        selectedItems.addAll(currentItemsText.split(", ").filter { it.isNotBlank() })

        val checkedItems = skincareOptions.map { selectedItems.contains(it) }.toBooleanArray()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose Skincare Items")

        builder.setMultiChoiceItems(skincareOptions, checkedItems) { _, which, isChecked ->
            val selectedItem = skincareOptions[which]
            if (isChecked) {
                if (!selectedItems.contains(selectedItem)) {
                    selectedItems.add(selectedItem)
                }
            } else {
                selectedItems.remove(selectedItem)
            }
        }

        builder.setPositiveButton("OK") { _, _ ->
            val sortedSelectedItems = selectedItems.sortedBy { skincareOptions.indexOf(it) }
            val selectedText = sortedSelectedItems.joinToString(", ")
            tvChooseSkincareItem.text = selectedText
        }

        builder.setNegativeButton("Cancel", null)

        builder.create().show()
    }

    private fun updateSubheaderTime() {
        val calendarNow = Calendar.getInstance()
        val selectedCalendar = Calendar.getInstance()

        selectedCalendar.set(Calendar.HOUR_OF_DAY, npHour.value)
        selectedCalendar.set(Calendar.MINUTE, npMinute.value)
        selectedCalendar.set(Calendar.SECOND, 0)

        if (selectedCalendar.timeInMillis <= calendarNow.timeInMillis) {
            selectedCalendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        val diffInMillis = selectedCalendar.timeInMillis - calendarNow.timeInMillis
        val diffInMinutes = (diffInMillis / (1000 * 60)).toInt()

        val days = diffInMinutes / (24 * 60)
        val hours = (diffInMinutes % (24 * 60)) / 60
        val minutes = (diffInMinutes % 60)

        tvSubheader.text = when {
            days > 0 -> "Alarm in $days day $hours hour $minutes minute"
            hours > 0 || minutes > 0 -> "Alarm in $hours hour $minutes minute"
            else -> "Alarm time has passed"
        }
    }

    private fun updateAlarmPeriod() {
        val hour = npHour.value
        tvAlarmPeriod.text = if (hour in 0..11) "Morning" else "Evening"
    }

    private fun validateInputs(): Boolean {
        val repeatText = tvRepeat.text.toString()
        val skincareItemsText = tvChooseSkincareItem.text.toString()

        if (repeatText.isBlank()) {
            Toast.makeText(this, "Please select repeat option", Toast.LENGTH_SHORT).show()
            return false
        }

        if (skincareItemsText.isBlank()) {
            Toast.makeText(this, "Please choose at least one skincare item", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

}

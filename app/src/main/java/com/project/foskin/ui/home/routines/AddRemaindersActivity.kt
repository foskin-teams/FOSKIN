package com.project.foskin.ui.home.routines

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.project.foskin.R
import com.project.foskin.databinding.ActivityAddRemaindersBinding
import java.util.Calendar

class AddRemaindersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRemaindersBinding
    private lateinit var alarmViewModel: AlarmViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddRemaindersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        alarmViewModel = ViewModelProvider(this)[AlarmViewModel::class.java]

        binding.npHour.minValue = 0
        binding.npHour.maxValue = 23
        binding.npHour.wrapSelectorWheel = true

        binding.npMinute.minValue = 0
        binding.npMinute.maxValue = 59
        binding.npMinute.wrapSelectorWheel = true

        setTimeToNow()

        binding.npHour.setOnValueChangedListener { _, _, _ ->
            updateSubheaderTime()
            updateAlarmPeriod()
        }

        binding.npMinute.setOnValueChangedListener { _, _, _ ->
            updateSubheaderTime()
        }

        binding.tvRepeat.setOnClickListener {
            showRepeatOptionsDialog()
        }

        binding.tvSkincareItems.setOnClickListener {
            showItemSkincareOptionsDialog()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {
            if (validateInputs()) {
                val hour = binding.npHour.value
                val minute = binding.npMinute.value
                val period = binding.tvAlarmPeriod.text.toString()
                val repeat = binding.tvRepeat.text.toString()
                val skincareItems = binding.tvChooseSkincareItem.text.toString()
                val vibrate = binding.switchVibrate.isChecked
                val deleteAfterRing = binding.switchDeleteAfterRinging.isChecked

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

                val savedAlarms = SharedPreferencesHelper.getAlarms(this)

                val existingAlarm = savedAlarms.find {
                    it.hour == hour && it.minute == minute
                }

                if (existingAlarm != null) {
                    AlertDialog.Builder(this)
                        .setTitle("Time Conflict")
                        .setMessage("An alarm is already set for ${String.format("%02d:%02d", hour, minute)}. Do you want to delete the previous alarm?")
                        .setPositiveButton("Yes") { dialog, _ ->
                            val updatedAlarms = savedAlarms.filter { it.id != existingAlarm.id }.toMutableList()
                            SharedPreferencesHelper.saveAlarms(this, updatedAlarms) // Save updated list

                            alarmViewModel.addAlarm(this, newAlarm)
                            updatedAlarms.add(newAlarm)
                            SharedPreferencesHelper.saveAlarms(this, updatedAlarms)

                            val intent = Intent(this, RemaindersActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)

                            Toast.makeText(this, "Alarm saved successfully!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                } else {
                    alarmViewModel.addAlarm(this, newAlarm)

                    val updatedAlarms = savedAlarms.toMutableList()
                    updatedAlarms.add(newAlarm)
                    SharedPreferencesHelper.saveAlarms(this, updatedAlarms)

                    val intent = Intent(this, RemaindersActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)

                    Toast.makeText(this, "Alarm saved successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun setTimeToNow() {
        val calendar = Calendar.getInstance()
        binding.npHour.value = calendar.get(Calendar.HOUR_OF_DAY)
        binding.npMinute.value = calendar.get(Calendar.MINUTE)
        updateSubheaderTime()
        updateAlarmPeriod()
    }

    private fun showRepeatOptionsDialog() {
        val repeatOptions = arrayOf("Once", "Daily")
        val currentSelection = repeatOptions.indexOf(binding.tvRepeat.text.toString())

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Repeat Options")
        builder.setSingleChoiceItems(repeatOptions, currentSelection) { dialog, which ->
            binding.tvRepeat.text = repeatOptions[which]
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
        val currentItemsText = binding.tvChooseSkincareItem.text.toString()
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
            binding.tvChooseSkincareItem.text = selectedText
        }

        builder.setNegativeButton("Cancel", null)

        builder.create().show()
    }

    private fun updateSubheaderTime() {
        val calendarNow = Calendar.getInstance()
        val selectedCalendar = Calendar.getInstance()

        selectedCalendar.set(Calendar.HOUR_OF_DAY, binding.npHour.value)
        selectedCalendar.set(Calendar.MINUTE, binding.npMinute.value)
        selectedCalendar.set(Calendar.SECOND, 0)

        if (selectedCalendar.timeInMillis <= calendarNow.timeInMillis) {
            selectedCalendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        val diffInMillis = selectedCalendar.timeInMillis - calendarNow.timeInMillis
        val diffInMinutes = (diffInMillis / (1000 * 60)).toInt()

        val days = diffInMinutes / (24 * 60)
        val hours = (diffInMinutes % (24 * 60)) / 60
        val minutes = (diffInMinutes % 60)

        if (calendarNow.get(Calendar.HOUR_OF_DAY) == binding.npHour.value &&
            calendarNow.get(Calendar.MINUTE) == binding.npMinute.value) {
            binding.tvSubheader.text = "Alarm time has passed\nAlarm in 23 hour 59 minute"
        }
        else if (diffInMillis <= 60000) {
            binding.tvSubheader.text = "Alarm in $hours hour $minutes minute"
        }
        else if (days > 0) {
            binding.tvSubheader.text = "Alarm in $days day $hours hour $minutes minute"
        } else if (hours > 0 || minutes > 0) {
            binding.tvSubheader.text = "Alarm in $hours hour $minutes minute"
        } else {
            binding.tvSubheader.text = "Alarm time has passed"
        }
    }

    private fun updateAlarmPeriod() {
        val hour = binding.npHour.value
        binding.tvAlarmPeriod.text = if (hour in 0..11) "Morning" else "Evening"
    }

    private fun validateInputs(): Boolean {
        val repeatText = binding.tvRepeat.text.toString()
        val skincareItemsText = binding.tvChooseSkincareItem.text.toString()

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

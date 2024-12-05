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
        npMinute.setOnValueChangedListener { _, _, _ ->
            updateSubheaderTime()
        }

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


    // Fungsi untuk menampilkan dialog pemilihan repeat (Once / Daily)
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

    // Fungsi untuk mengupdate subheader time
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

    // Fungsi untuk mengupdate alarm period
    private fun updateAlarmPeriod() {
        val hour = npHour.value
        tvAlarmPeriod.text = if (hour in 0..11) "Morning" else "Evening"
    }
}

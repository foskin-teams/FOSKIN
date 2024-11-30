package com.project.foskin.ui.remainders

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.project.foskin.R

class RemaindersActivity : AppCompatActivity() {

    private lateinit var timePicker: TimePicker
    private lateinit var spinnerSound: Spinner
    private lateinit var spinnerRepeat: Spinner
    private lateinit var checkVibrate: CheckBox
    private lateinit var checkDeleteAfterRinging: CheckBox
    private lateinit var editLabel: EditText
    private lateinit var buttonSaveAlarm: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_remainders)

        supportActionBar?.hide()

        // Initialize views
        timePicker = findViewById(R.id.timePicker)
        spinnerSound = findViewById(R.id.spinnerSound)
        spinnerRepeat = findViewById(R.id.spinnerRepeat)
        checkVibrate = findViewById(R.id.checkVibrate)
        checkDeleteAfterRinging = findViewById(R.id.checkDeleteAfterRinging)
        editLabel = findViewById(R.id.editLabel)
        buttonSaveAlarm = findViewById(R.id.buttonSaveAlarm)

        // Set onClick for save button
        buttonSaveAlarm.setOnClickListener {
            saveAlarm()
        }
    }

    private fun saveAlarm() {
        val hour = timePicker.hour
        val minute = timePicker.minute
        val sound = spinnerSound.selectedItem.toString()
        val repeat = spinnerRepeat.selectedItem.toString()
        val vibrate = checkVibrate.isChecked
        val deleteAfterRinging = checkDeleteAfterRinging.isChecked
        val label = editLabel.text.toString()

        // Show a toast with the alarm details
        val alarmDetails = "Alarm Set for $hour:$minute\nSound: $sound\nRepeat: $repeat\nVibrate: $vibrate\nDelete after ringing: $deleteAfterRinging\nLabel: $label"
        Toast.makeText(this, alarmDetails, Toast.LENGTH_LONG).show()

        // Here you can save the alarm in your database or scheduling system
    }
}
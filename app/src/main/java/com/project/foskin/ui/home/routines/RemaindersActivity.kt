package com.project.foskin.ui.home.routines

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.foskin.R

class RemaindersActivity : AppCompatActivity() {

    private lateinit var alarmViewModel: AlarmViewModel
    private lateinit var alarmAdapter: AlarmAdapter
    private lateinit var tvRemove: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remainders)
        supportActionBar?.hide()

        val tvAddNotification: TextView = findViewById(R.id.tv_add_notification)
        val btnBack: TextView = findViewById(R.id.tvBackRemainders)
        val rvNextIntake: RecyclerView = findViewById(R.id.rv_next_intake)
        val tvNextIntakeEmpty: TextView = findViewById(R.id.tv_next_intake_empty)
        tvRemove = findViewById(R.id.tv_remove)  // Initialize tv_remove

        alarmViewModel = ViewModelProvider(this)[AlarmViewModel::class.java]

        alarmAdapter = AlarmAdapter(mutableListOf()) { updatedAlarm ->
            alarmViewModel.addAlarm(this, updatedAlarm) // Update SharedPreferences
            updateRemoveVisibility()  // Check visibility of tv_remove after state change
        }

        rvNextIntake.adapter = alarmAdapter
        rvNextIntake.layoutManager = LinearLayoutManager(this)

        alarmViewModel.alarms.observe(this) { alarms ->
            val sortedAlarms = alarms.sortedBy { it.timeInMillis() }
            alarmAdapter.updateData(sortedAlarms)
            toggleEmptyState(rvNextIntake, tvNextIntakeEmpty, sortedAlarms.isEmpty())

            // Ensure visibility of tv_remove after updating the list
            updateRemoveVisibility()
        }

        tvAddNotification.setOnClickListener {
            // Navigate to AddRemaindersActivity
            startActivity(Intent(this, AddRemaindersActivity::class.java))
        }

        btnBack.setOnClickListener {
            finish()
        }

        alarmViewModel.loadAlarms(this)

        // Handle click event for tv_remove
        tvRemove.setOnClickListener {
            val checkedAlarms = alarmAdapter.getCheckedItems() // Get checked alarms
            checkedAlarms.forEach { alarm ->
                alarmViewModel.removeAlarm(this, alarm) // Remove each checked alarm
            }

            // After removal, update the visibility of tv_remove
            updateRemoveVisibility()
        }
    }

    private fun updateRemoveVisibility() {
        // Show or hide tv_remove based on whether any checkbox is checked
        val hasCheckedItems = alarmAdapter.hasCheckedItems() // This method checks for checked items
        if (hasCheckedItems) {
            tvRemove.visibility = View.VISIBLE
        } else {
            tvRemove.visibility = View.GONE
        }
    }

    private fun toggleEmptyState(recyclerView: RecyclerView, emptyTextView: TextView, isEmpty: Boolean) {
        if (isEmpty) {
            recyclerView.visibility = View.GONE
            emptyTextView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyTextView.visibility = View.GONE
        }
    }
}

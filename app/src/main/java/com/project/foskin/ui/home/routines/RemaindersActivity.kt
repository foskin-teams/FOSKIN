package com.project.foskin.ui.home.routines

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.project.foskin.R

class RemaindersActivity : AppCompatActivity() {

    private lateinit var alarmViewModel: AlarmViewModel
    private lateinit var alarmAdapter: AlarmAdapter
    private lateinit var tvRemove: TextView
    private lateinit var tvNextIntakeEmpty: TextView
    private lateinit var tvPastIntakeEmpty: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var rvNextIntake: RecyclerView
    private lateinit var rvPastIntake: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remainders)
        supportActionBar?.hide()

        val tvAddNotification: TextView = findViewById(R.id.tv_add_notification)
        val btnBack: TextView = findViewById(R.id.tvBackRemainders)
        rvNextIntake = findViewById(R.id.rv_next_intake)
        rvPastIntake = findViewById(R.id.rv_past_intake)
        tvNextIntakeEmpty = findViewById(R.id.tv_next_intake_empty)
        tvPastIntakeEmpty = findViewById(R.id.tv_past_intake_empty)
        tvRemove = findViewById(R.id.tv_remove)

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)

        alarmViewModel = ViewModelProvider(this)[AlarmViewModel::class.java]

        alarmAdapter = AlarmAdapter(mutableListOf()) { updatedAlarm ->
            alarmViewModel.addAlarm(this, updatedAlarm)
            updateRemoveVisibility()
        }

        rvNextIntake.adapter = alarmAdapter
        rvNextIntake.layoutManager = LinearLayoutManager(this)

        alarmViewModel.alarms.observe(this) { alarms ->
            alarmAdapter.updateData(alarms.sortedBy { it.timeInMillis() })
            toggleEmptyState(rvNextIntake, tvNextIntakeEmpty, alarms.isEmpty())
            updateRemoveVisibility()
        }

        // Past intake RecyclerView
        val pastAlarms = mutableListOf<AlarmData>() // Get past alarms here
        toggleEmptyState(rvPastIntake, tvPastIntakeEmpty, pastAlarms.isEmpty())

        tvAddNotification.setOnClickListener {
            startActivity(Intent(this, AddRemaindersActivity::class.java))
        }

        btnBack.setOnClickListener {
            finish()
        }

        alarmViewModel.loadAlarms(this)

        alarmViewModel.startAutoRefresh(this)

        tvRemove.setOnClickListener {
            val checkedAlarms = alarmAdapter.getCheckedItems()
            checkedAlarms.forEach { alarm ->
                alarmViewModel.removeAlarm(this, alarm)
            }

            updateRemoveVisibility()
        }

        swipeRefreshLayout.setEnabled(false)

        hideRecyclerView(true)
    }

    private fun updateRemoveVisibility() {
        val hasCheckedItems = alarmAdapter.hasCheckedItems()
        tvRemove.visibility = if (hasCheckedItems) View.VISIBLE else View.GONE
    }

    private fun toggleEmptyState(recyclerView: RecyclerView, emptyTextView: TextView, isEmpty: Boolean) {
        recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
        emptyTextView.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun hideRecyclerView(hide: Boolean) {
        if (hide) {
            rvNextIntake.visibility = View.INVISIBLE
            swipeRefreshLayout.setEnabled(false)
        } else {
            rvNextIntake.visibility = View.VISIBLE
            swipeRefreshLayout.setEnabled(true)
        }
    }
}

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
    private lateinit var tvRemove: TextView
    private lateinit var tvNextIntakeEmpty: TextView
    private lateinit var tvPastIntakeEmpty: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var swipeRefreshLayoutPast: SwipeRefreshLayout
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
        swipeRefreshLayoutPast = findViewById(R.id.swipe_refresh_layout_intake)

        alarmViewModel = ViewModelProvider(this)[AlarmViewModel::class.java]

        val upcomingAlarmAdapter = UpcomingAlarmAdapter(
            alarms = mutableListOf(),
            onAlarmChecked = {
                updateRemoveVisibility()
            }
        )

        rvNextIntake.adapter = upcomingAlarmAdapter
        rvNextIntake.layoutManager = LinearLayoutManager(this)

        val pastAlarmAdapter = PastAlarmAdapter(
            alarms = mutableListOf(),
            onAlarmChecked = {
                updateRemoveVisibility()
            }
        )

        rvPastIntake.adapter = pastAlarmAdapter
        rvPastIntake.layoutManager = LinearLayoutManager(this)

        alarmViewModel.alarms.observe(this) { alarms ->
            val currentTimeMillis = System.currentTimeMillis()

            val upcomingAlarms = alarms.filter { it.timeInMillis() > currentTimeMillis }
            val pastAlarms = alarms.filter { it.timeInMillis() <= currentTimeMillis }

            (rvNextIntake.adapter as UpcomingAlarmAdapter).updateData(upcomingAlarms)

            (rvPastIntake.adapter as PastAlarmAdapter).updateData(pastAlarms)

            toggleEmptyState(rvNextIntake, tvNextIntakeEmpty, upcomingAlarms.isEmpty())
            toggleEmptyState(rvPastIntake, tvPastIntakeEmpty, pastAlarms.isEmpty())
        }


        tvRemove.setOnClickListener {
            val checkedAlarms = mutableListOf<AlarmData>()
            checkedAlarms.addAll((rvNextIntake.adapter as UpcomingAlarmAdapter).getCheckedItems())
            checkedAlarms.addAll((rvPastIntake.adapter as PastAlarmAdapter).getCheckedItems())

            checkedAlarms.forEach { alarm ->
                alarmViewModel.removeAlarm(this, alarm)
            }

            (rvNextIntake.adapter as UpcomingAlarmAdapter).clearCheckedItems()
            (rvPastIntake.adapter as PastAlarmAdapter).clearCheckedItems()
            updateRemoveVisibility()
        }

        tvAddNotification.setOnClickListener {
            startActivity(Intent(this, AddRemaindersActivity::class.java))
        }

        btnBack.setOnClickListener {
            finish()
        }

        alarmViewModel.loadAlarms(this)
        alarmViewModel.startAutoRefresh(this)

        swipeRefreshLayout.setOnRefreshListener {
            alarmViewModel.loadAlarms(this)
            swipeRefreshLayout.isRefreshing = false
        }

        // Disable swipe for past intake
        swipeRefreshLayoutPast.isEnabled = false

        hideRecyclerView(true)
    }

    private fun updateRemoveVisibility() {
        val hasCheckedItems = (rvNextIntake.adapter as UpcomingAlarmAdapter).hasCheckedItems() ||
                (rvPastIntake.adapter as PastAlarmAdapter).hasCheckedItems()
        tvRemove.visibility = if (hasCheckedItems) View.VISIBLE else View.GONE
    }

    private fun toggleEmptyState(recyclerView: RecyclerView, emptyTextView: TextView, isEmpty: Boolean) {
        recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
        emptyTextView.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun hideRecyclerView(hide: Boolean) {
        if (hide) {
            rvNextIntake.visibility = View.INVISIBLE
            rvPastIntake.visibility = View.INVISIBLE
            swipeRefreshLayout.isEnabled = false
            swipeRefreshLayoutPast.isEnabled = false
        } else {
            rvNextIntake.visibility = View.VISIBLE
            rvPastIntake.visibility = View.VISIBLE
            swipeRefreshLayout.isEnabled = true
            swipeRefreshLayoutPast.isEnabled = false
        }
    }
}

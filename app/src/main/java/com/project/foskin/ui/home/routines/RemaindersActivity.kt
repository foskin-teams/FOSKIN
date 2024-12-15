package com.project.foskin.ui.home.routines

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.foskin.databinding.ActivityRemaindersBinding

class RemaindersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRemaindersBinding
    private lateinit var alarmViewModel: AlarmViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRemaindersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        alarmViewModel = ViewModelProvider(this)[AlarmViewModel::class.java]

        val upcomingAlarmAdapter = UpcomingAlarmAdapter(
            alarms = mutableListOf(),
            onAlarmChecked = { updateRemoveVisibility() }
        )

        binding.rvNextIntake.apply {
            adapter = upcomingAlarmAdapter
            layoutManager = LinearLayoutManager(this@RemaindersActivity)
        }

        val pastAlarmAdapter = PastAlarmAdapter(
            alarms = mutableListOf(),
            onAlarmChecked = { updateRemoveVisibility() }
        )

        binding.rvPastIntake.apply {
            adapter = pastAlarmAdapter
            layoutManager = LinearLayoutManager(this@RemaindersActivity)
        }

        alarmViewModel.alarms.observe(this) { alarms ->
            val currentTimeMillis = System.currentTimeMillis()

            val upcomingAlarms = alarms.filter { it.timeInMillis() > currentTimeMillis }
            val pastAlarms = alarms.filter { it.timeInMillis() <= currentTimeMillis }

            upcomingAlarmAdapter.updateData(upcomingAlarms)
            pastAlarmAdapter.updateData(pastAlarms)

            toggleEmptyState(binding.rvNextIntake, binding.tvNextIntakeEmpty, upcomingAlarms.isEmpty())
            toggleEmptyState(binding.rvPastIntake, binding.tvPastIntakeEmpty, pastAlarms.isEmpty())
        }

        binding.tvRemove.setOnClickListener {
            val checkedAlarms = mutableListOf<AlarmData>().apply {
                addAll(upcomingAlarmAdapter.getCheckedItems())
                addAll(pastAlarmAdapter.getCheckedItems())
            }

            checkedAlarms.forEach { alarm -> alarmViewModel.removeAlarm(this, alarm) }

            upcomingAlarmAdapter.clearCheckedItems()
            pastAlarmAdapter.clearCheckedItems()
            updateRemoveVisibility()
        }

        binding.tvAddNotification.setOnClickListener {
            startActivity(Intent(this, AddRemaindersActivity::class.java))
        }

        binding.tvBackRemainders.setOnClickListener { finish() }

        alarmViewModel.loadAlarms(this)
        alarmViewModel.startAutoRefresh(this)

        binding.swipeRefreshLayout.setOnRefreshListener {
            alarmViewModel.loadAlarms(this)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.swipeRefreshLayoutIntake.isEnabled = false

        hideRecyclerView(true)
    }

    private fun updateRemoveVisibility() {
        val hasCheckedItems = (binding.rvNextIntake.adapter as UpcomingAlarmAdapter).hasCheckedItems() ||
                (binding.rvPastIntake.adapter as PastAlarmAdapter).hasCheckedItems()
        binding.tvRemove.visibility = if (hasCheckedItems) View.VISIBLE else View.GONE
    }

    private fun toggleEmptyState(
        recyclerView: View,
        emptyTextView: View,
        isEmpty: Boolean
    ) {
        recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
        emptyTextView.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun hideRecyclerView(hide: Boolean) {
        if (hide) {
            binding.rvNextIntake.visibility = View.INVISIBLE
            binding.rvPastIntake.visibility = View.INVISIBLE
            binding.swipeRefreshLayout.isEnabled = false
            binding.swipeRefreshLayoutIntake.isEnabled = false
        } else {
            binding.rvNextIntake.visibility = View.VISIBLE
            binding.rvPastIntake.visibility = View.VISIBLE
            binding.swipeRefreshLayout.isEnabled = true
            binding.swipeRefreshLayoutIntake.isEnabled = false
        }
    }
}

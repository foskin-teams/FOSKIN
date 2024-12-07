package com.project.foskin.ui.home.routines

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AlarmViewModel : ViewModel() {
    private val _alarms = MutableLiveData<List<AlarmData>>()
    val alarms: LiveData<List<AlarmData>> get() = _alarms

    private val handler = Handler(Looper.getMainLooper())
    private val refreshInterval = 1000L

    fun loadAlarms(context: Context) {
        val alarms = SharedPreferencesHelper.getAlarms(context)
        _alarms.value = alarms.sortedBy { it.timeInMillis() }
    }

    fun startAutoRefresh(context: Context) {
        handler.postDelayed(object : Runnable {
            override fun run() {
                loadAlarms(context)
                handler.postDelayed(this, refreshInterval)
            }
        }, refreshInterval)
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(null)
    }

    fun addAlarm(context: Context, alarm: AlarmData) {
        val currentAlarms = SharedPreferencesHelper.getAlarms(context).toMutableList()
        val existingAlarmIndex = currentAlarms.indexOfFirst { it.id == alarm.id }

        if (existingAlarmIndex != -1) {
            currentAlarms[existingAlarmIndex] = alarm
        } else {
            currentAlarms.add(alarm)
        }

        SharedPreferencesHelper.saveAlarms(context, currentAlarms)
        loadAlarms(context)
    }

    fun removeAlarm(context: Context, alarm: AlarmData) {
        val currentAlarms = SharedPreferencesHelper.getAlarms(context).toMutableList()
        val alarmIndex = currentAlarms.indexOfFirst { it.id == alarm.id }
        if (alarmIndex != -1) {
            currentAlarms.removeAt(alarmIndex)
            SharedPreferencesHelper.saveAlarms(context, currentAlarms)
            loadAlarms(context)
        }
    }
}

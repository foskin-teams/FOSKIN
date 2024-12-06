package com.project.foskin.ui.home.routines

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AlarmViewModel : ViewModel() {
    private val _alarms = MutableLiveData<List<AlarmData>>()
    val alarms: LiveData<List<AlarmData>> get() = _alarms

    fun loadAlarms(context: Context) {
        val alarms = SharedPreferencesHelper.getAlarms(context)
        _alarms.value = alarms.sortedBy { it.timeInMillis() }
    }

    fun addAlarm(context: Context, alarm: AlarmData) {
        val currentAlarms = SharedPreferencesHelper.getAlarms(context).toMutableList()
        val existingAlarmIndex = currentAlarms.indexOfFirst { it.id == alarm.id }

        if (existingAlarmIndex != -1) {
            // Replace the existing alarm with the updated one
            currentAlarms[existingAlarmIndex] = alarm
        } else {
            // Add new alarm if it doesn't exist
            currentAlarms.add(alarm)
        }

        SharedPreferencesHelper.saveAlarms(context, currentAlarms)
        _alarms.postValue(currentAlarms)
    }

    fun removeAlarm(context: Context, alarm: AlarmData) {
        val currentAlarms = SharedPreferencesHelper.getAlarms(context).toMutableList()
        val alarmIndex = currentAlarms.indexOfFirst { it.id == alarm.id }
        if (alarmIndex != -1) {
            currentAlarms.removeAt(alarmIndex)
            SharedPreferencesHelper.saveAlarms(context, currentAlarms)
            _alarms.postValue(currentAlarms)
        }
    }
}

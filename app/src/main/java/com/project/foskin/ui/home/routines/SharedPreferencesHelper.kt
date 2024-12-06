package com.project.foskin.ui.home.routines

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPreferencesHelper {

    private const val PREF_NAME = "AlarmPreferences"
    private const val ALARM_LIST_KEY = "AlarmList"

    fun saveAlarms(context: Context, alarms: List<AlarmData>) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(alarms)
        editor.putString(ALARM_LIST_KEY, json)
        editor.apply()
    }

    fun getAlarms(context: Context): List<AlarmData> {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(ALARM_LIST_KEY, null)
        val type = object : TypeToken<List<AlarmData>>() {}.type
        val alarms = Gson().fromJson<List<AlarmData>>(json, type) ?: emptyList()

        return alarms
    }
}

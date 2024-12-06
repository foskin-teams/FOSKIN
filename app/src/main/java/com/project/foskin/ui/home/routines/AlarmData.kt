package com.project.foskin.ui.home.routines

import java.util.Calendar

data class AlarmData(
    val id: Long,  // ID unik untuk setiap alarm
    val hour: Int,
    val minute: Int,
    val period: String,
    val repeat: String,
    val skincareItems: String,
    val vibrate: Boolean,
    val deleteAfterRinging: Boolean
)

// Fungsi untuk mengembalikan waktu alarm dalam milidetik
fun AlarmData.timeInMillis(): Long {
    val now = Calendar.getInstance()
    val alarmTime = Calendar.getInstance()

    alarmTime.set(Calendar.HOUR_OF_DAY, this.hour)
    alarmTime.set(Calendar.MINUTE, this.minute)
    alarmTime.set(Calendar.SECOND, 0)

    if (alarmTime.before(now)) {
        alarmTime.add(Calendar.DAY_OF_YEAR, 1)
    }

    return alarmTime.timeInMillis - now.timeInMillis
}

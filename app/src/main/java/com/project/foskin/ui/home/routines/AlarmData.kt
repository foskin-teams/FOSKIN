package com.project.foskin.ui.home.routines

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Calendar

@Parcelize
data class AlarmData(
    val id: Long,
    val hour: Int,
    val minute: Int,
    val period: String,
    val repeat: String,
    val skincareItems: String,
    val vibrate: Boolean,
    val deleteAfterRinging: Boolean
) : Parcelable

fun AlarmData.timeInMillis(): Long {
    val alarmTime = Calendar.getInstance()
    alarmTime.set(Calendar.HOUR_OF_DAY, this.hour)
    alarmTime.set(Calendar.MINUTE, this.minute)
    alarmTime.set(Calendar.SECOND, 0)

    return alarmTime.timeInMillis
}


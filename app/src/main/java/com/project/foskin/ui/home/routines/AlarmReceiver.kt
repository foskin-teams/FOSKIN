package com.project.foskin.ui.home.routines

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.project.foskin.R

class AlarmReceiver : BroadcastReceiver() {

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra("alarm_message") ?: "Alarm is ringing!"
        val vibrate = intent.getBooleanExtra("vibrate", false)
        val deleteAfterRing = intent.getBooleanExtra("delete_after_ring", false)
        val alarmId = intent.getLongExtra("alarm_id", -1)

        if (vibrate) {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            val vibrationDuration = (1000..5000).random()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(vibrationDuration.toLong(), VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(vibrationDuration.toLong())
            }
        }

        val notification = NotificationCompat.Builder(context, "alarm_channel")
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("Alarm Notification")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(alarmId.toInt(), notification)

        if (deleteAfterRing && alarmId != -1L) {
            val savedAlarms = SharedPreferencesHelper.getAlarms(context).toMutableList()
            val updatedAlarms = savedAlarms.filter { it.id != alarmId }
            SharedPreferencesHelper.saveAlarms(context, updatedAlarms)
        }
    }

}

package com.project.foskin.ui.home.routines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.foskin.R

class AlarmAdapter(
    private var alarms: MutableList<AlarmData>,
    private val onAlarmChecked: (AlarmData) -> Unit
) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    val checkedItems = mutableSetOf<AlarmData>()

    inner class AlarmViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDetails: TextView = view.findViewById(R.id.tvDetails)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val tvRepeat: TextView = view.findViewById(R.id.tvRepeat)
        val tvPeriode: TextView = view.findViewById(R.id.tvPeriode)
        val ivCheck: ImageButton = view.findViewById(R.id.iv_check)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_routine, parent, false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = alarms[position]

        val formattedTime = String.format("%02d:%02d", alarm.hour, alarm.minute)

        holder.tvDetails.text = alarm.skincareItems
        holder.tvTime.text = formattedTime
        holder.tvRepeat.text = alarm.repeat
        holder.tvPeriode.text = alarm.period

        val checkIcon = if (alarm.vibrate) {
            R.drawable.check_circle_remainders
        } else {
            R.drawable.check_circle_outline
        }

        holder.ivCheck.setImageResource(checkIcon)

        holder.ivCheck.setOnClickListener {
            val updatedAlarm = alarm.copy(vibrate = !alarm.vibrate)

            val index = alarms.indexOfFirst { it == alarm }
            if (index != -1) {
                alarms[index] = updatedAlarm
            }

            if (updatedAlarm.vibrate) {
                checkedItems.add(updatedAlarm)
            } else {
                checkedItems.remove(updatedAlarm)
            }

            notifyItemChanged(position)

            onAlarmChecked(updatedAlarm)
        }
    }

    override fun getItemCount(): Int = alarms.size

    fun updateData(newAlarms: List<AlarmData>) {
        this.alarms = newAlarms.toMutableList()
        notifyDataSetChanged()
    }

    fun hasCheckedItems(): Boolean = checkedItems.isNotEmpty()

    fun getCheckedItems(): List<AlarmData> = checkedItems.toList()
}

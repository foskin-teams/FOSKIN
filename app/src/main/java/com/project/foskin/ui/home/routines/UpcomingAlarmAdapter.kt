package com.project.foskin.ui.home.routines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.foskin.R

class UpcomingAlarmAdapter(
    private var alarms: MutableList<AlarmData>,
    private val onAlarmChecked: (AlarmData) -> Unit
) : RecyclerView.Adapter<UpcomingAlarmAdapter.UpcomingAlarmViewHolder>() {

    private val checkedItems = mutableSetOf<AlarmData>()

    inner class UpcomingAlarmViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDetails: TextView = view.findViewById(R.id.tvDetails)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val tvRepeat: TextView = view.findViewById(R.id.tvRepeat)
        val tvPeriode: TextView = view.findViewById(R.id.tvPeriode)
        val ivCheck: ImageButton = view.findViewById(R.id.iv_check)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingAlarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_routine, parent, false)
        return UpcomingAlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: UpcomingAlarmViewHolder, position: Int) {
        val alarm = alarms[position]
        val formattedTime = String.format("%02d:%02d", alarm.hour, alarm.minute)

        holder.tvDetails.text = alarm.skincareItems
        holder.tvTime.text = formattedTime
        holder.tvRepeat.text = alarm.repeat
        holder.tvPeriode.text = alarm.period

        val checkIcon = if (checkedItems.contains(alarm)) {
            R.drawable.check_circle_remainders
        } else {
            R.drawable.check_circle_outline
        }
        holder.ivCheck.setImageResource(checkIcon)

        holder.ivCheck.setOnClickListener {
            toggleCheck(alarm, position)
            onAlarmChecked(alarm)
        }
    }

    override fun getItemCount(): Int = alarms.size

    private fun toggleCheck(alarm: AlarmData, position: Int) {
        if (checkedItems.contains(alarm)) {
            checkedItems.remove(alarm)
        } else {
            checkedItems.add(alarm)
        }
        notifyItemChanged(position)
    }

    fun getCheckedItems(): List<AlarmData> = checkedItems.toList()

    fun hasCheckedItems(): Boolean = checkedItems.isNotEmpty()

    fun clearCheckedItems() {
        checkedItems.clear()
        notifyDataSetChanged()
    }

    fun updateData(newAlarms: List<AlarmData>) {
        alarms.clear()
        alarms.addAll(newAlarms.sortedBy { it.timeInMillis() })
        notifyDataSetChanged()
    }
}

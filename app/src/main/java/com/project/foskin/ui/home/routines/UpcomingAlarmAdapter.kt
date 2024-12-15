package com.project.foskin.ui.home.routines

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.foskin.R
import com.project.foskin.databinding.ItemRoutineBinding

class UpcomingAlarmAdapter(
    private var alarms: MutableList<AlarmData>,
    private val onAlarmChecked: (AlarmData) -> Unit
) : RecyclerView.Adapter<UpcomingAlarmAdapter.UpcomingAlarmViewHolder>() {

    private val checkedItems = mutableSetOf<AlarmData>()

    inner class UpcomingAlarmViewHolder(val binding: ItemRoutineBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingAlarmViewHolder {
        val binding = ItemRoutineBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UpcomingAlarmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UpcomingAlarmViewHolder, position: Int) {
        val alarm = alarms[position]
        val formattedTime = String.format("%02d:%02d", alarm.hour, alarm.minute)

        with(holder.binding) {
            tvDetails.text = alarm.skincareItems
            tvTime.text = formattedTime
            tvRepeat.text = alarm.repeat
            tvPeriode.text = alarm.period

            val checkIcon = if (checkedItems.contains(alarm)) {
                R.drawable.check_circle_remainders
            } else {
                R.drawable.check_circle_outline
            }
            ivCheck.setImageResource(checkIcon)

            ivCheck.setOnClickListener {
                toggleCheck(alarm, position)
                onAlarmChecked(alarm)
            }
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

package com.project.foskin.ui.home.routines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.project.foskin.R
import com.project.foskin.databinding.ItemRoutineBinding

class PastAlarmAdapter(
    private var alarms: MutableList<AlarmData>,
    private val onAlarmChecked: (AlarmData) -> Unit
) : RecyclerView.Adapter<PastAlarmAdapter.PastAlarmViewHolder>() {

    private val checkedItems = mutableSetOf<AlarmData>()

    inner class PastAlarmViewHolder(private val binding: ItemRoutineBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(alarm: AlarmData, position: Int) {
            val formattedTime = String.format("%02d:%02d", alarm.hour, alarm.minute)

            binding.tvDetails.text = alarm.skincareItems
            binding.tvTime.text = formattedTime
            binding.tvRepeat.text = alarm.repeat
            binding.tvPeriode.text = alarm.period

            binding.vBackgroundRoutine.setBackgroundColor(itemView.context.getColor(R.color.purple_500))

            itemView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

            val checkIcon = if (checkedItems.contains(alarm)) {
                R.drawable.check_circle_remainders
            } else {
                R.drawable.check_circle_outline
            }
            binding.ivCheck.setImageResource(checkIcon)

            binding.ivCheck.setOnClickListener {
                toggleCheck(alarm, position)
                onAlarmChecked(alarm)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastAlarmViewHolder {
        val binding = ItemRoutineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PastAlarmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PastAlarmViewHolder, position: Int) {
        val alarm = alarms[position]
        holder.bind(alarm, position)
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
        alarms.addAll(newAlarms)

        if (alarms.size > 10) {
            alarms.sortBy { it.timeInMillis() }
            alarms = alarms.takeLast(10).toMutableList()
        }

        notifyDataSetChanged()
    }
}
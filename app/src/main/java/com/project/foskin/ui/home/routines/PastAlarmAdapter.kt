package com.project.foskin.ui.home.routines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.foskin.R

class PastAlarmAdapter(
    private var alarms: MutableList<AlarmData>,
    private val onAlarmChecked: (AlarmData) -> Unit
) : RecyclerView.Adapter<PastAlarmAdapter.PastAlarmViewHolder>() {

    private val checkedItems = mutableSetOf<AlarmData>()

    inner class PastAlarmViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDetails: TextView = view.findViewById(R.id.tvDetails)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val tvRepeat: TextView = view.findViewById(R.id.tvRepeat)
        val tvPeriode: TextView = view.findViewById(R.id.tvPeriode)
        val ivCheck: ImageButton = view.findViewById(R.id.iv_check)
        val vBackgroundRoutine: View = view.findViewById(R.id.vBackgroundRoutine)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastAlarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_routine, parent, false)
        return PastAlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: PastAlarmViewHolder, position: Int) {
        val alarm = alarms[position]
        val formattedTime = String.format("%02d:%02d", alarm.hour, alarm.minute)

        holder.tvDetails.text = alarm.skincareItems
        holder.tvTime.text = formattedTime
        holder.tvRepeat.text = alarm.repeat
        holder.tvPeriode.text = alarm.period

        // Set background color to purple for past alarms
        holder.vBackgroundRoutine.setBackgroundColor(holder.itemView.context.getColor(R.color.purple_500))

        // Add blur effect (or a simulated layer effect for now)
        holder.itemView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

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
        // Update data in the adapter
        alarms.clear()
        alarms.addAll(newAlarms)

        // If the size exceeds 4, remove the oldest items
        if (alarms.size > 4) {
            alarms.sortBy { it.timeInMillis() }  // Sort based on time (assuming timeInMillis returns a timestamp)
            alarms = alarms.takeLast(4).toMutableList()  // Keep only the 4 most recent items
        }

        notifyDataSetChanged()
    }
}

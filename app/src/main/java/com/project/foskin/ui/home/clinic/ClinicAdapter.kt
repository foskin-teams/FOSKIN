package com.project.foskin.ui.home.clinic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.foskin.R
import com.project.foskin.databinding.ItemClinicBinding

class ClinicAdapter(
    private val clinics: List<Clinic>,
    private val onClinicClick: (Clinic) -> Unit
) : RecyclerView.Adapter<ClinicAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding = ItemClinicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClinicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val clinic = clinics[position]
        holder.bind(clinic)
        holder.itemView.setOnClickListener {
            onClinicClick(clinic)
        }
    }

    override fun getItemCount(): Int = clinics.size

    class ClinicViewHolder(private val binding: ItemClinicBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clinic: Clinic) {
            binding.tvTitle.text = clinic.name
            binding.tvAddress.text = clinic.description
            binding.tvRating.text = clinic.rating.toString()
            binding.tvReview.text = "${clinic.reviews} reviews"

            Glide.with(itemView.context)
                .load(clinic.imageResId)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_error)
                .into(binding.ivImage)

            val isOpen = isClinicOpen(clinic.operationalHours)

            if (isOpen) {
                binding.tvHours.setBackgroundResource(R.drawable.bg_button_open)
                binding.tvHours.text = "Open"
            } else {
                binding.tvHours.setBackgroundResource(R.drawable.bg_button_closed)
                binding.tvHours.text = "Closed"
            }
        }

        private fun isClinicOpen(operationalHours: OperationalHours): Boolean {
            val currentHour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
            val currentDayOfWeek = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK)

            val todayOpeningHours = when (currentDayOfWeek) {
                java.util.Calendar.MONDAY -> operationalHours.weekday["Monday"]
                java.util.Calendar.SATURDAY -> operationalHours.weekend["Saturday"]
                java.util.Calendar.SUNDAY -> operationalHours.weekend["Sunday"]
                else -> operationalHours.weekday["Other"]
            }

            val openingTime = todayOpeningHours?.substringBefore("-")?.trim()?.toIntOrNull()
            val closingTime = todayOpeningHours?.substringAfter("-")?.trim()?.toIntOrNull()

            return openingTime != null && closingTime != null && currentHour in openingTime..closingTime
        }
    }
}

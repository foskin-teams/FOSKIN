package com.project.foskin.ui.home.clinic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.foskin.R
import com.project.foskin.databinding.ItemClinicBinding

class ClinicAdapter(
    private val clinics: List<Clinic>,
    private val onClinicClick: (Clinic) -> Unit
) : RecyclerView.Adapter<ClinicAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_clinic, parent, false)
        return ClinicViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val clinic = clinics[position]
        holder.bind(clinic)
        holder.itemView.setOnClickListener {
            onClinicClick(clinic)
        }
    }

    override fun getItemCount(): Int = clinics.size

    class ClinicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
        private val tvRating: TextView = itemView.findViewById(R.id.tvRating)
        private val tvReview: TextView = itemView.findViewById(R.id.tvReview)
        private val ivImage: ImageView = itemView.findViewById(R.id.iv_image)
        private val binding = ItemClinicBinding.bind(itemView)

        fun bind(clinic: Clinic) {
            tvTitle.text = clinic.name
            tvAddress.text = clinic.description
            tvRating.text = clinic.rating.toString()
            tvReview.text = "${clinic.reviews} reviews"
            Glide.with(itemView.context)
                .load(clinic.imageResId)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_error)
                .into(ivImage)

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

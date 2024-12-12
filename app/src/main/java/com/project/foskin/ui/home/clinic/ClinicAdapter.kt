package com.project.foskin.ui.home.clinic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.foskin.R

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

        fun bind(clinic: Clinic) {
            tvTitle.text = clinic.name
            tvAddress.text = clinic.address
            tvRating.text = clinic.rating.toString()
            tvReview.text = "${clinic.reviews} reviews"
            ivImage.setImageResource(clinic.imageResId)
        }
    }
}

package com.project.foskin.ui.home.promo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.foskin.databinding.ItemPromoBinding

class PromoAdapter(private val promoList: List<Promo>) :
    RecyclerView.Adapter<PromoAdapter.PromoViewHolder>() {

    inner class PromoViewHolder(private val binding: ItemPromoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(promo: Promo) {
            binding.ivImage.setImageResource(promo.imageResId)
            binding.tvTitle.text = promo.title
            binding.tvDesc.text = promo.description
            binding.tvDate.text = promo.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromoViewHolder {
        val binding = ItemPromoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PromoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PromoViewHolder, position: Int) {
        holder.bind(promoList[position])
    }

    override fun getItemCount(): Int = promoList.size
}

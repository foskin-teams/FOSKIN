package com.project.foskin.ui.shop

import android.view.LayoutInflater
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.foskin.R

class MenuSkincareAdapter(
    private val menuList: List<MenuItemSkincare>,
    private val onItemClicked: (MenuItemSkincare) -> Unit
) : RecyclerView.Adapter<MenuSkincareAdapter.MenuViewHolder>() {

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val menuImage: ImageView = view.findViewById(R.id.menuImage)
        val menuName: TextView = view.findViewById(R.id.menuName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu_skincare, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menuItem = menuList[position]
        holder.menuName.text = menuItem.name
        Glide.with(holder.menuImage.context)
            .load(menuItem.imageResId)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_error)
            .into(holder.menuImage)
        holder.itemView.setOnClickListener { onItemClicked(menuItem) }
    }

    override fun getItemCount(): Int = menuList.size
}


package com.project.foskin.ui.shop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
        holder.menuImage.setImageResource(menuItem.imageResId) // Replace with your image logic
        holder.itemView.setOnClickListener { onItemClicked(menuItem) }
    }

    override fun getItemCount(): Int = menuList.size
}


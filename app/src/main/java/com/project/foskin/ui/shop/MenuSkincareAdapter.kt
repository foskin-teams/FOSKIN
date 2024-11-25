package com.project.foskin.ui.shop

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.foskin.R

class MenuSkincareAdapter(private val menuList: List<MenuItemSkincare>, private val onItemClick: (MenuItem) -> Unit) :
    RecyclerView.Adapter<MenuSkincareAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu_skincare, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = menuList[position]
        holder.textView.text = item.title
        holder.imageView.setImageResource(item.backgroundImageRes)
        holder.itemView.setOnClickListener {
            onItemClick(item) // Menangani klik item
        }
    }

    private fun onItemClick(item: MenuItemSkincare) {

    }

    override fun getItemCount(): Int = menuList.size

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageMenu)
        val textView: TextView = view.findViewById(R.id.textMenu)
    }
}

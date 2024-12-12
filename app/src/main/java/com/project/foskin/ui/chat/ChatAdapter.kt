package com.project.foskin.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.foskin.R

class ChatAdapter(private val chatList: List<ChatItem>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profileImage: ImageView = view.findViewById(R.id.profile_image)
        val name: TextView = view.findViewById(R.id.tvName)
        val time: TextView = view.findViewById(R.id.tvTime)
        val message: TextView = view.findViewById(R.id.tvChat)
        val notificationBadge: TextView = view.findViewById(R.id.chat_notification_badge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val item = chatList[position]
        holder.profileImage.setImageResource(item.profileImage)
        holder.name.text = item.name
        holder.time.text = item.time
        holder.message.text = item.message

        if (item.notificationCount > 0) {
            holder.notificationBadge.text = item.notificationCount.toString()
            holder.notificationBadge.visibility = View.VISIBLE
        } else {
            holder.notificationBadge.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = chatList.size
}

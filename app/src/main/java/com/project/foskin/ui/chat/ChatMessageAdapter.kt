package com.project.foskin.ui.chat

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.foskin.R

class ChatMessageAdapter(
    private var messages: List<ChatAI>
) : RecyclerView.Adapter<ChatMessageAdapter.ChatMessageViewHolder>() {

    class ChatMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageTextView: TextView = view.findViewById(R.id.messageText)
        val messageImageView: ImageView = view.findViewById(R.id.messageImage)
        val messageContainer: View = view.findViewById(R.id.messageContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return ChatMessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        val chat = messages[position]

        holder.messageTextView.text = chat.prompt

        if (chat.bitmap != null) {
            holder.messageImageView.setImageBitmap(chat.bitmap)
            holder.messageImageView.visibility = View.VISIBLE
        } else {
            holder.messageImageView.visibility = View.GONE
        }

        val layoutParams = holder.messageContainer.layoutParams as FrameLayout.LayoutParams
        if (chat.isFromUser) {
            layoutParams.gravity = Gravity.END
            holder.messageContainer.setBackgroundResource(R.drawable.bg_message_user)
        } else {
            layoutParams.gravity = Gravity.START
            holder.messageContainer.setBackgroundResource(R.drawable.bg_message_bot)
        }
        holder.messageContainer.layoutParams = layoutParams
    }

    override fun getItemCount(): Int = messages.size

    fun updateMessages(newMessages: List<ChatAI>) {
        val previousSize = messages.size
        messages = newMessages
        notifyItemInserted(previousSize)
    }
}

package com.project.foskin.ui.chat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.foskin.R

class ChatFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        recyclerView = view.findViewById(R.id.rvAllChat)
        setupRecyclerView()
        return view
    }

    private fun setupRecyclerView() {
        val chatList = listOf(
            ChatItem(
                profileImage = R.drawable.chatbot,
                name = "Chat with AI",
                time = "3:02PM",
                message = "Hi! I'm here to help. Ask me anything or share what you need!",
                notificationCount = 0
            ),
            ChatItem(
                profileImage = R.drawable.marcello,
                name = "Marcello Ilham",
                time = "3:02PM",
                message = "Hello! How are you doing? I hope everything is going well with your current project.",
                notificationCount = 1
            )
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ChatAdapter(chatList) { chatItem ->
            val intent = Intent(requireContext(), ChatActivity::class.java)
            intent.putExtra("CHAT_NAME", chatItem.name)
            startActivity(intent)
        }

    }

}

package com.project.foskin.ui.chat

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
        val ChatList = listOf(
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
                message = "Hello! How are you doing? I hope everything is going well with your current project. Let me know if you need any assistance with the tasks that are pending. Also, don't forget that we need to discuss the upcoming deadlines during our meeting next week.",
                notificationCount = 1
            )
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ChatAdapter(ChatList)
    }
}

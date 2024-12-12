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
        val dummyChatList = listOf(
            ChatItem(
                profileImage = R.drawable.marcello,
                name = "Marcello Ilham",
                time = "3:02PM",
                message = "Hello! How are you doing? I hope everything is going well with your current project. Let me know if you need any assistance with the tasks that are pending. Also, don't forget that we need to discuss the upcoming deadlines during our meeting next week.",
                notificationCount = 1
            ),
            ChatItem(
                profileImage = R.drawable.marcello,
                name = "Jane Doe",
                time = "2:45PM",
                message = "Did you finish the report? I have reviewed the initial draft, and there are a few points that need clarification. I'll send you the feedback after our discussion later. We need to ensure everything is aligned before the presentation tomorrow.",
                notificationCount = 3
            ),
            ChatItem(
                profileImage = R.drawable.marcello,
                name = "John Smith",
                time = "Yesterday",
                message = "Meeting rescheduled to 5 PM. The client has confirmed their availability, so we will need to finalize the agenda and ensure that all team members are prepared for the presentation. I’ll send out a meeting invite with the new details.",
                notificationCount = 0
            )
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ChatAdapter(dummyChatList)
    }
}

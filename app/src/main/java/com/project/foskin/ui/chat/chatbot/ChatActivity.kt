package com.project.foskin.ui.chat.chatbot

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.project.foskin.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val chatName = intent.getStringExtra("CHAT_NAME") ?: "ChatBot"
        binding.tvAllChat.text = chatName

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
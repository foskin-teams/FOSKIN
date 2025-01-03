package com.project.foskin.ui.chat.chatbot

import android.graphics.Bitmap
import com.project.foskin.ui.chat.ChatAI

data class ChatState (
    val chatList: MutableList<ChatAI> = mutableListOf(),
    val prompt: String = "",
    val bitmap: Bitmap? = null
)

package com.project.foskin.ui.chat

import android.graphics.Bitmap

data class ChatItem(
    val profileImage: Int,
    val name: String,
    val time: String,
    val message: String,
    val notificationCount: Int
)

data class ChatAI (
    val prompt: String,
    val bitmap: Bitmap?,
    val isFromUser: Boolean
)


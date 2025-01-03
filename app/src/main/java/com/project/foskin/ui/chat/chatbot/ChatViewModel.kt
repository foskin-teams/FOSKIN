package com.project.foskin.ui.chat.chatbot

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.foskin.ui.chat.ChatAI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val _chatState = MutableStateFlow(ChatState())
    val chatState = _chatState.asStateFlow()

    fun onEvent(event: ChatUiEvent) {
        when (event) {
            is ChatUiEvent.SendPrompt -> {
                if (event.prompt?.isNotEmpty() == true) {
                    event.prompt?.let { addPrompt(it, event.bitmap) }

                    if (event.bitmap != null) {
                        event.prompt?.let { getResponseWithImage(it, event.bitmap) }
                    } else {
                        getResponse(event.prompt)
                    }
                }
            }

            is ChatUiEvent.UpdatePrompt -> {
                _chatState.update {
                    it.copy(prompt = event.newPrompt)
                }
            }
        }
    }

    private fun addPrompt(prompt: String, bitmap: Bitmap?) {
        _chatState.update {
            it.copy(
                chatList = it.chatList.toMutableList().apply {
                    add(ChatAI(prompt, bitmap, isFromUser = true))
                },
                prompt = "",
                bitmap = null
            )
        }
    }

    private fun getResponse(prompt: String) {
        viewModelScope.launch {
            val chat = ChatAIData.getResponse(prompt)
            _chatState.update {
                it.copy(
                    chatList = it.chatList.toMutableList().apply {
                        add(chat.copy(isFromUser = false))
                    }
                )
            }
        }
    }

    private fun getResponseWithImage(prompt: String, bitmap: Bitmap) {
        Log.d("ChatViewModel", "Received bitmap: ${bitmap}")
        viewModelScope.launch {
            val chat = ChatAIData.getResponseWithImage(prompt, bitmap)
            _chatState.update {
                it.copy(
                    chatList = it.chatList.toMutableList().apply {
                        add(0, chat)
                    }
                )
            }
        }
    }
}

package com.project.foskin.ui.chat

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.foskin.databinding.ActivityChatBinding
import com.project.foskin.ui.chat.chatbot.ChatUiEvent
import com.project.foskin.ui.chat.chatbot.ChatViewModel
import kotlinx.coroutines.flow.collectLatest

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var chatAdapter: ChatMessageAdapter

//    private val pickImageLauncher = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        if (result.resultCode == RESULT_OK && result.data != null) {
//            val imageUri: Uri? = result.data?.data
//            imageUri?.let {
//                val inputStream = contentResolver.openInputStream(it)
//                val bitmap = BitmapFactory.decodeStream(inputStream)
//
//                // Kirim gambar ke ViewModel
//                viewModel.onEvent(ChatUiEvent.SendPrompt(null, bitmap))
//            }
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val chatName = intent.getStringExtra("CHAT_NAME") ?: "ChatBot"
        binding.tvAllChat.text = chatName

        // Initialize adapter
        chatAdapter = ChatMessageAdapter(emptyList())
        binding.rvAllChat.adapter = chatAdapter
        binding.rvAllChat.layoutManager = LinearLayoutManager(this)

        binding.btnBack.setOnClickListener {
            finish()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.chatState.collectLatest { state ->
                chatAdapter.updateMessages(state.chatList)
                binding.rvAllChat.scrollToPosition(state.chatList.size - 1)
            }
        }

        binding.btnSend.setOnClickListener {
            val message = binding.etMessageInput.text.toString()
            if (message.isNotEmpty()) {
                viewModel.onEvent(ChatUiEvent.SendPrompt(message, null))
                binding.etMessageInput.text.clear()
            }
        }
    }

//    private fun openGallery() {
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        pickImageLauncher.launch(intent)
//    }
}
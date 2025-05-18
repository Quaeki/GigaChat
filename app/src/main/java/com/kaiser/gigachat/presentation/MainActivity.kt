package com.kaiser.gigachat.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kaiser.gigachat.data.api.RetrofitClient
import com.kaiser.gigachat.data.model.ChatModel
import com.kaiser.gigachat.data.repository.ChatRepositoryImpl
import com.kaiser.gigachat.domain.usecase.SendMessageUseCase
import com.kaiser.gigachat.presentation.chat.ChatScreen
import com.kaiser.gigachat.presentation.chat.ChatViewModel
import com.kaiser.gigachat.ui.theme.GigaChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Инициализация зависимостей
        val chatApi = RetrofitClient.chatApi
        val chatModel = ChatModel(chatApi)
        val chatRepository = ChatRepositoryImpl(chatModel)
        val sendMessageUseCase = SendMessageUseCase(chatRepository)
        val viewModel = ChatViewModel(sendMessageUseCase)

        setContent {
            GigaChatTheme {
                ChatScreen(viewModel)
            }
        }
    }
}
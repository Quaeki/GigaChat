package com.kaiser.gigachat.presentation.chat

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaiser.gigachat.domain.model.Message
import com.kaiser.gigachat.domain.usecase.SendMessageUseCase
import kotlinx.coroutines.launch

class ChatViewModel(private val sendMessageUseCase: SendMessageUseCase) : ViewModel() {
    var state = mutableStateOf(ChatState())
        private set

    fun onMessageInputChanged(text: String) {
        state.value = state.value.copy(inputText = text)
    }

    fun onSendMessage() {
        val userMessage = state.value.inputText
        if (userMessage.isBlank()) return

        state.value = state.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val aiResponse = sendMessageUseCase(userMessage)
                val newMessage = Message(userMessage, aiResponse)
                state.value = state.value.copy(
                    messages = listOf(newMessage) + state.value.messages,
                    inputText = "",
                    isLoading = false
                )
            } catch (e: Exception) {
                state.value = state.value.copy(
                    isLoading = false,
                    inputText = "Ошибка: ${e.message ?: "Неизвестная ошибка"}"
                )
            }
        }
    }
}
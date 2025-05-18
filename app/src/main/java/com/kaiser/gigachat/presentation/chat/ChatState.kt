package com.kaiser.gigachat.presentation.chat

import com.kaiser.gigachat.domain.model.Message

data class ChatState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val inputText: String = "",
    val availableModels: List<String> = emptyList(),
    val error: String? = null
)
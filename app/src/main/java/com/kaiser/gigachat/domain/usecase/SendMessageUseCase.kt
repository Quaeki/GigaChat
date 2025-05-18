package com.kaiser.gigachat.domain.usecase

import com.kaiser.gigachat.domain.repository.ChatRepository

class SendMessageUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke(message: String): String {
        if (message.isBlank()) throw IllegalArgumentException("Сообщение не может быть пустым")
        return repository.sendMessage(message)
    }
}
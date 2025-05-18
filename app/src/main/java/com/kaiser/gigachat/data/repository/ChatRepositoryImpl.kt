package com.kaiser.gigachat.data.repository

import com.kaiser.gigachat.data.model.ChatModel
import com.kaiser.gigachat.domain.repository.ChatRepository

class ChatRepositoryImpl(private val chatModel: ChatModel) : ChatRepository {
    override suspend fun sendMessage(message: String): String {
        return chatModel.sendMessage(message)
    }
    override suspend fun getModels(): List<String> {
        return chatModel.getModels()
    }
}
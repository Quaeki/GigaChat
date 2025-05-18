package com.kaiser.gigachat.data.model

import ChatApi
import ChatRequest
import Message
import java.io.IOException

class ChatModel(private val api: ChatApi) {
    suspend fun sendMessage(
        message: String,
        model: String = "deepseek-chat",
        stream: Boolean = false
    ): String {
        if (message.isBlank()) {
            throw IllegalArgumentException("Сообщение не может быть пустым")
        }
        val request = ChatRequest(
            messages = listOf(Message(role = "user", content = message)),
            model = model,
            stream = stream
        )
        try {
            val response = api.sendMessage(request)
            return response.choices.firstOrNull()?.message?.content
                ?: throw Exception("Ответ от сервера не содержит сообщений")
        } catch (e: IOException) {
            throw IOException("Ошибка соединения: ${e.message}", e)
        } catch (e: Exception) {
            throw Exception("Неизвестная ошибка: ${e.message}", e)
        }
    }

    suspend fun getModels(): List<String> {
        try {
            val response = api.getModels()
            return response.data.map { it.id }
        } catch (e: IOException) {
            throw IOException("Ошибка соединения: ${e.message}", e)
        } catch (e: Exception) {
            throw Exception("Неизвестная ошибка: ${e.message}", e)
        }
    }
}
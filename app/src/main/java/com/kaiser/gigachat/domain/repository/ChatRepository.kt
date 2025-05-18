package com.kaiser.gigachat.domain.repository

interface ChatRepository {
    suspend fun sendMessage(message: String): String
    suspend fun getModels(): List<String>
}
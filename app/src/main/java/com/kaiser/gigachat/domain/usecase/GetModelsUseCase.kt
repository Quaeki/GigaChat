package com.kaiser.gigachat.domain.usecase

import com.kaiser.gigachat.domain.repository.ChatRepository

class GetModelsUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke(): List<String> {
        return repository.getModels()
    }
}
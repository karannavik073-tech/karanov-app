package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val sessionId: String,
    val sender: String, // "USER" or "AI"
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val rawSources: String = "", // serialized "title:::uri|||title:::uri"
    val searchQueries: String = "", // comma-separated search queries
    val languageCode: String = "en",
    val isRealTimeGrounded: Boolean = false
) {
    val isUser: Boolean get() = sender == "USER"
    val isAi: Boolean get() = sender == "AI"

    fun getGroundingSources(): List<GroundingSource> {
        if (rawSources.isBlank()) return emptyList()
        return rawSources.split("|||").mapNotNull { item ->
            val parts = item.split(":::")
            if (parts.size >= 2) {
                GroundingSource(title = parts[0], uri = parts[1])
            } else null
        }
    }

    companion object {
        fun formatSources(sources: List<GroundingSource>): String {
            return sources.joinToString("|||") { "${it.title}:::${it.uri}" }
        }
    }
}

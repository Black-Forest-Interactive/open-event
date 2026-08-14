package de.sambalmueslie.openevent.infrastructure.telegram

import com.fasterxml.jackson.annotation.JsonProperty

data class TelegramSendMessageRequest(
    @JsonProperty("chat_id")
    val chatId: String,
    val text: String,
)
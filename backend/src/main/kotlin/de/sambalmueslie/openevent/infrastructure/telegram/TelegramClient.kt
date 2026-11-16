package de.sambalmueslie.openevent.infrastructure.telegram

import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client

@Client("https://api.telegram.org")
interface TelegramClient {

    @Post("/bot{token}/sendMessage")
    fun sendMessage(@PathVariable token: String, @Body request: TelegramSendMessageRequest): TelegramSendMessageResponse

}


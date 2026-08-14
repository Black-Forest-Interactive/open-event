package de.sambalmueslie.openevent.core.notification.handler

import de.sambalmueslie.openevent.config.TelegramConfig
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.event.api.EventStatus
import de.sambalmueslie.openevent.core.share.ShareCrudService
import de.sambalmueslie.openevent.core.share.api.Share
import de.sambalmueslie.openevent.core.share.api.ShareInfo
import de.sambalmueslie.openevent.infrastructure.telegram.TelegramClient
import de.sambalmueslie.openevent.infrastructure.telegram.TelegramNotificationHandler
import de.sambalmueslie.openevent.infrastructure.telegram.TelegramSendMessageRequest
import de.sambalmueslie.openevent.infrastructure.telegram.TelegramSendMessageResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class TelegramNotificationHandlerTest {

    private val eventService = mockk<EventCrudService>(relaxed = true)
    private val shareService = mockk<ShareCrudService>(relaxed = true)
    private val telegramClient = mockk<TelegramClient>(relaxed = true)
    private val telegramConfig = TelegramConfig()

    private val actor = mockk<Account>(relaxed = true)
    private val event = Event(
        id = 1,
        owner = AccountInfo(1, "owner", "", "owner@test.de", "First", "Last", "de"),
        start = LocalDateTime.of(2026, 1, 1, 10, 0),
        finish = LocalDateTime.of(2026, 1, 1, 12, 0),
        title = "Test Event",
        shortText = "short",
        longText = "long",
        imageUrl = "",
        iconUrl = "",
        featured = false,
        hasLocation = false,
        hasRegistration = false,
        status = EventStatus.ACTIVE,
        published = true,
        tags = emptySet(),
        created = LocalDateTime.now(),
        changed = null,
    )

    private fun createHandler(): TelegramNotificationHandler {
        telegramConfig.botToken = "token"
        telegramConfig.chatId = "chat-id"
        return TelegramNotificationHandler(eventService, shareService, telegramClient, telegramConfig)
    }

    @Test
    fun registersItselfAsEventListener() {
        val handler = createHandler()
        verify { eventService.register(handler) }
    }

    @Test
    fun doesNotSendWhenDisabled() {
        telegramConfig.enabled = false
        val handler = createHandler()

        handler.handleCreated(actor, event)

        verify(exactly = 0) { telegramClient.sendMessage(any(), any()) }
    }

    @Test
    fun sendsMessageOnCreateWhenEnabled() {
        telegramConfig.enabled = true
        every { telegramClient.sendMessage(any(), any()) } returns TelegramSendMessageResponse(true)
        val handler = createHandler()

        handler.handleCreated(actor, event)

        val slot = mutableListOf<TelegramSendMessageRequest>()
        verify { telegramClient.sendMessage("token", capture(slot)) }
        assertEquals("chat-id", slot.single().chatId)
        assert(slot.single().text.contains("Test Event"))
    }

    @Test
    fun sendsMessageOnDelete() {
        telegramConfig.enabled = true
        every { telegramClient.sendMessage(any(), any()) } returns TelegramSendMessageResponse(true)
        val handler = createHandler()

        handler.handleDeleted(actor, event)

        verify { telegramClient.sendMessage("token", match { it.text.contains("gelöscht") }) }
    }

    @Test
    fun sendsMessageOnCancelWithReason() {
        telegramConfig.enabled = true
        every { telegramClient.sendMessage(any(), any()) } returns TelegramSendMessageResponse(true)
        val handler = createHandler()

        handler.canceled(actor, event, "bad weather")

        verify { telegramClient.sendMessage("token", match { it.text.contains("bad weather") }) }
    }

    @Test
    fun doesNotSendOnUnpublish() {
        telegramConfig.enabled = true
        val handler = createHandler()

        handler.publishedChanged(actor, event.copy(published = false))

        verify(exactly = 0) { telegramClient.sendMessage(any(), any()) }
    }

    @Test
    fun appendsShareLinkWhenSharingEnabled() {
        telegramConfig.enabled = true
        val share = Share("share-key", event.id, true, LocalDateTime.now(), null)
        every { shareService.findByEventId(event.id) } returns share
        every { shareService.getInfo(event, event.owner) } returns ShareInfo(share, "https://open.psm.church/event/share-key")
        every { telegramClient.sendMessage(any(), any()) } returns TelegramSendMessageResponse(true)
        val handler = createHandler()

        handler.handleCreated(actor, event)

        verify { telegramClient.sendMessage("token", match { it.text.contains("https://open.psm.church/event/share-key") }) }
    }

    @Test
    fun omitsShareLinkWhenSharingDisabled() {
        telegramConfig.enabled = true
        val share = Share("share-key", event.id, false, LocalDateTime.now(), null)
        every { shareService.findByEventId(event.id) } returns share
        every { telegramClient.sendMessage(any(), any()) } returns TelegramSendMessageResponse(true)
        val handler = createHandler()

        handler.handleCreated(actor, event)

        val slot = mutableListOf<TelegramSendMessageRequest>()
        verify { telegramClient.sendMessage("token", capture(slot)) }
        assertFalse(slot.single().text.contains("http"))
    }

    @Test
    fun continuesWhenClientThrows() {
        telegramConfig.enabled = true
        every { telegramClient.sendMessage(any(), any()) } throws RuntimeException("boom")
        val handler = createHandler()

        handler.handleCreated(actor, event)

        verify { telegramClient.sendMessage(any(), any()) }
    }

}

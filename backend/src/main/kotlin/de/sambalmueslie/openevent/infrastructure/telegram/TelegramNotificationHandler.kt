package de.sambalmueslie.openevent.infrastructure.telegram

import de.sambalmueslie.openevent.config.TelegramConfig
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.announcement.api.Announcement
import de.sambalmueslie.openevent.core.event.EventChangeListener
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.Event
import io.micronaut.context.annotation.Context
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Context
class TelegramNotificationHandler(
    eventService: EventCrudService,
    private val telegramClient: TelegramClient,
    private val telegramConfig: TelegramConfig,
) : EventChangeListener {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(TelegramNotificationHandler::class.java)
    }

    init {
        eventService.register(this)
    }

    override fun handleCreated(actor: Account, obj: Event) {
        send("🆕 Neue Veranstaltung: ${obj.title}\n📅 ${obj.format()}")
    }

    override fun handleUpdated(actor: Account, obj: Event) {
        send("✏️ Veranstaltung geändert: ${obj.title}")
    }

    override fun handleDeleted(actor: Account, obj: Event) {
        send("🗑️ Veranstaltung gelöscht: ${obj.title}")
    }

    override fun publishedChanged(actor: Account, event: Event) {
        if (event.published) {
            send("📢 Veranstaltung veröffentlicht: ${event.title}")
        }
    }

    override fun canceled(actor: Account, event: Event, reason: String) {
        send("❌ Veranstaltung abgesagt: ${event.title}\nGrund: $reason")
    }

    override fun statusChanged(actor: Account, event: Event) {
        // intentionally left empty
    }

    override fun featuredChanged(actor: Account, event: Event) {
        // intentionally left empty
    }

    override fun titleChanged(actor: Account, event: Event) {
        // intentionally left empty
    }

    override fun shortTextChanged(actor: Account, event: Event) {
        // intentionally left empty
    }

    override fun longTextChanged(actor: Account, event: Event) {
        // intentionally left empty
    }

    override fun tagsChanged(actor: Account, event: Event) {
        // intentionally left empty
    }

    override fun textChanged(actor: Account, event: Event) {
        // intentionally left empty
    }

    override fun categoryChanged(actor: Account, event: Event) {
        // intentionally left empty
    }

    override fun audienceChanged(actor: Account, event: Event) {
        // intentionally left empty
    }

    override fun announcementAdded(actor: Account, event: Event, announcement: Announcement) {
        // intentionally left empty
    }

    override fun announcementRemoved(actor: Account, event: Event, announcement: Announcement) {
        // intentionally left empty
    }

    private fun send(text: String) {
        if (!telegramConfig.enabled) return
        try {
            telegramClient.sendMessage(telegramConfig.botToken, TelegramSendMessageRequest(telegramConfig.chatId, text))
        } catch (e: Exception) {
            logger.error("Failed to send telegram message", e)
        }
    }

}

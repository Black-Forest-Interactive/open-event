package de.sambalmueslie.openevent.core.logic.history.handler


import de.sambalmueslie.openevent.core.logic.event.EventChangeListener
import de.sambalmueslie.openevent.core.logic.event.EventCrudService
import de.sambalmueslie.openevent.core.logic.history.HistoryCrudService
import de.sambalmueslie.openevent.core.model.*
import io.micronaut.context.annotation.Context
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Context
class EventNotificationHandler(
    eventService: EventCrudService,
    private val service: HistoryCrudService,
) : EventChangeListener {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EventNotificationHandler::class.java)
        const val KEY_EVENT_CREATED = "event.create"
        const val KEY_EVENT_UPDATED = "event.update"
        const val KEY_EVENT_DELETED = "event.delete"
        const val KEY_EVENT_PUBLISHED = "event.publish"
        const val KEY_EVENT_UNPUBLISHED = "event.unpublish"
    }


    init {
        eventService.register(this)
    }

    override fun handleCreated(actor: Account, obj: Event) {
        val request = HistoryEntryChangeRequest(
            HistoryEntryType.EVENT_CREATED, KEY_EVENT_CREATED, HistoryEntrySource.EVENT, ""
        )
        service.create(actor, obj, request)
    }

    override fun handleUpdated(actor: Account, obj: Event) {
        val request = HistoryEntryChangeRequest(
            HistoryEntryType.EVENT_CHANGED, KEY_EVENT_UPDATED, HistoryEntrySource.EVENT, ""
        )
        service.create(actor, obj, request)
    }


    override fun publishedChanged(actor: Account, event: Event) {
        val request = if (event.published) {
            HistoryEntryChangeRequest(
                HistoryEntryType.EVENT_CHANGED, KEY_EVENT_PUBLISHED, HistoryEntrySource.EVENT, ""
            )
        } else {
            HistoryEntryChangeRequest(
                HistoryEntryType.EVENT_CHANGED, KEY_EVENT_UNPUBLISHED, HistoryEntrySource.EVENT, ""
            )
        }
        service.create(actor, event, request)
    }


}

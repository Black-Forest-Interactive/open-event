package de.sambalmueslie.openevent.core.notification.handler

import de.sambalmueslie.openevent.core.event.api.Event

data class EventCancelContent(val event: Event, val reason: String)
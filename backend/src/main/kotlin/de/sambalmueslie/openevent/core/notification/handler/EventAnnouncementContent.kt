package de.sambalmueslie.openevent.core.notification.handler

import de.sambalmueslie.openevent.core.announcement.api.Announcement
import de.sambalmueslie.openevent.core.event.api.Event

data class EventAnnouncementContent(val event: Event, val announcement: Announcement)
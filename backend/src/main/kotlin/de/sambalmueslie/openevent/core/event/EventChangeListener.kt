package de.sambalmueslie.openevent.core.event

import de.sambalmueslie.openevent.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.announcement.api.Announcement
import de.sambalmueslie.openevent.core.event.api.Event

interface EventChangeListener : BusinessObjectChangeListener<Long, Event> {
    fun statusChanged(actor: Account, event: Event)
    fun publishedChanged(actor: Account, event: Event)
    fun canceled(actor: Account, event: Event, reason: String)
    fun featuredChanged(actor: Account, event: Event)
    fun titleChanged(actor: Account, event: Event)
    fun shortTextChanged(actor: Account, event: Event)
    fun longTextChanged(actor: Account, event: Event)
    fun tagsChanged(actor: Account, event: Event)
    fun textChanged(actor: Account, event: Event)
    fun categoryChanged(actor: Account, event: Event)
    fun audienceChanged(actor: Account, event: Event)
    fun announcementAdded(actor: Account, event: Event, announcement: Announcement)
    fun announcementRemoved(actor: Account, event: Event, announcement: Announcement)
}

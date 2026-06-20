package de.sambalmueslie.openevent.core.event

import de.sambalmueslie.openevent.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.event.api.Event

interface EventChangeListener : BusinessObjectChangeListener<Long, Event> {
    fun featuredChanged(actor: Account, event: Event)
    fun publishedChanged(actor: Account, event: Event)

}

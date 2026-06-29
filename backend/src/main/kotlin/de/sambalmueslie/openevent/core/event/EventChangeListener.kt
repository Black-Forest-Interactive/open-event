package de.sambalmueslie.openevent.core.event

import de.sambalmueslie.openevent.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.event.api.Event

interface EventChangeListener : BusinessObjectChangeListener<Long, Event> {
    fun publishedChanged(actor: Account, event: Event)
    fun featuredChanged(actor: Account, event: Event)
    fun titleChanged(actor: Account, event: Event)
    fun shortTextChanged(actor: Account, event: Event)
    fun longTextChanged(actor: Account, event: Event)
    fun tagsChanged(actor: Account, event: Event)

}

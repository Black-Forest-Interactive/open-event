package de.sambalmueslie.openevent.core.link

import de.sambalmueslie.openevent.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.link.api.Link

interface LinkChangeListener : BusinessObjectChangeListener<String, Link> {
    fun enabledChanged(actor: Account, link: Link)

}
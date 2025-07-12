package de.sambalmueslie.openevent.core.share

import de.sambalmueslie.openevent.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.share.api.Share

interface ShareChangeListener : BusinessObjectChangeListener<String, Share> {
    fun enabledChanged(actor: Account, share: Share)

}

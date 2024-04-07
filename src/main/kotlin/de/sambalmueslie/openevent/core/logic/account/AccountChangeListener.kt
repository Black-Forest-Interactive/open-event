package de.sambalmueslie.openevent.core.logic.account

import de.sambalmueslie.openevent.core.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.logic.account.api.Account

interface AccountChangeListener : BusinessObjectChangeListener<Long, Account>

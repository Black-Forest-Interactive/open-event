package de.sambalmueslie.openevent.core.logic.account

import de.sambalmueslie.openevent.core.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.model.Account

interface AccountChangeListener : BusinessObjectChangeListener<Long, Account>

package de.sambalmueslie.openevent.core.account

import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.logic.account.api.Account
import de.sambalmueslie.openevent.core.logic.account.api.Preferences
import de.sambalmueslie.openevent.core.logic.account.api.PreferencesChangeRequest

interface PreferencesStorage : Storage<Long, Preferences, PreferencesChangeRequest> {
    fun create(request: PreferencesChangeRequest, account: Account): Preferences
    fun findByAccount(account: Account): Preferences?
}

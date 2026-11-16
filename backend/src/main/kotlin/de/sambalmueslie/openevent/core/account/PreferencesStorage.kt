package de.sambalmueslie.openevent.core.account

import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.Preferences
import de.sambalmueslie.openevent.core.account.api.PreferencesChangeRequest
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable

interface PreferencesStorage : Storage<Long, Preferences, PreferencesChangeRequest> {
    fun create(request: PreferencesChangeRequest, account: Account): Preferences
    fun findByAccount(account: Account): Preferences?
    fun findByEmailNotificationsEnabled(pageable: Pageable): Page<Preferences>
}

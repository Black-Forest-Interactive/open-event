package de.sambalmueslie.openevent.api

import de.sambalmueslie.openevent.core.model.Preferences
import de.sambalmueslie.openevent.core.model.PreferencesChangeRequest
import io.micronaut.security.authentication.Authentication

interface PreferencesAPI : CrudAPI<Long, Preferences, PreferencesChangeRequest> {
    companion object {
        const val PERMISSION_READ = "openevent.preferences.read"
        const val PERMISSION_WRITE = "openevent.preferences.write"
        const val PERMISSION_ADMIN = "openevent.preferences.admin"
    }
    fun getOwn(auth: Authentication): Preferences?
}

package de.sambalmueslie.openevent.api

import de.sambalmueslie.openevent.core.model.PatchRequest
import de.sambalmueslie.openevent.core.model.Setting
import de.sambalmueslie.openevent.core.model.SettingChangeRequest
import io.micronaut.security.authentication.Authentication

interface SettingsAPI : CrudAPI<Long, Setting, SettingChangeRequest> {
    companion object {
        const val PERMISSION_READ = "openevent.settings.read"
        const val PERMISSION_WRITE = "openevent.settings.write"

        const val SETTINGS_URL_HELP = "url.help"
        const val SETTINGS_URL_TERMS_AND_CONDITIONS = "url.terms-and-conditions"
        const val SETTINGS_TEXT_TITLE = "text.title"
        const val SETTINGS_MAIL_FROM_ADDRESS = "mail.from-address"
        const val SETTINGS_MAIL_REPLY_TO_ADDRESS = "mail.reply-to-address"
        const val SETTINGS_MAIL_DEFAULT_ADMIN_ADDRESS = "mail.default-admin-address"
    }

    fun setValue(auth: Authentication, id: Long, value: PatchRequest<Any>): Setting?
    fun findByKey(auth: Authentication, key: String): Setting?
}


package de.sambalmueslie.openevent.gateway.external.settings

import de.sambalmueslie.openevent.infrastructure.settings.SettingsService
import de.sambalmueslie.openevent.infrastructure.settings.api.TextResponse
import de.sambalmueslie.openevent.infrastructure.settings.api.UrlResponse
import jakarta.inject.Singleton

@Singleton
class ExternalSettingsService(
    private val settingsService: SettingsService
) {

    fun getTitle() = TextResponse(settingsService.getTitle())
    fun getPortalUrl() = UrlResponse(settingsService.getPortalUrl())

}
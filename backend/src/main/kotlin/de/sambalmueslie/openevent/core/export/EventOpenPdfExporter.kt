package de.sambalmueslie.openevent.core.export

import de.sambalmueslie.openevent.infrastructure.settings.SettingsService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class EventOpenPdfExporter(
    settingsService: SettingsService,
    timeProvider: TimeProvider
) : BasePdfExporter("templates/event-flyer-v3.vm", settingsService, timeProvider, logger) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EventOpenPdfExporter::class.java)
    }

}
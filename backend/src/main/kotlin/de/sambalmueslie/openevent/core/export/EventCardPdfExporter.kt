package de.sambalmueslie.openevent.core.export

import de.sambalmueslie.openevent.infrastructure.settings.SettingsService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class EventCardPdfExporter(
    settingsService: SettingsService,
    timeProvider: TimeProvider
) : BasePdfExporter("templates/event-cards-v2.vm", settingsService, timeProvider, logger) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EventCardPdfExporter::class.java)
    }
}

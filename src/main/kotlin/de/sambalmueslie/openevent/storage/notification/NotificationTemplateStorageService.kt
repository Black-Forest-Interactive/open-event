package de.sambalmueslie.openevent.storage.notification


import de.sambalmueslie.openevent.core.model.NotificationScheme
import de.sambalmueslie.openevent.core.model.NotificationTemplate
import de.sambalmueslie.openevent.core.model.NotificationTemplateChangeRequest
import de.sambalmueslie.openevent.core.storage.NotificationTemplateStorage
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import de.sambalmueslie.openevent.storage.BaseStorageService
import de.sambalmueslie.openevent.storage.SimpleDataObjectConverter
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class NotificationTemplateStorageService(
    private val repository: NotificationTemplateRepository,
    cacheService: CacheService,
    private val timeProvider: TimeProvider,
) : BaseStorageService<Long, NotificationTemplate, NotificationTemplateChangeRequest, NotificationTemplateData>(
    repository,
    SimpleDataObjectConverter(),
    cacheService,
    NotificationTemplate::class,
    logger
), NotificationTemplateStorage {


    companion object {
        private val logger: Logger = LoggerFactory.getLogger(NotificationTemplateStorageService::class.java)
        private const val SCHEME_REFERENCE = "scheme"
    }


    override fun create(scheme: NotificationScheme, request: NotificationTemplateChangeRequest): NotificationTemplate {
        return create(request, mapOf(Pair(SCHEME_REFERENCE, scheme)))
    }

    override fun createData(
        request: NotificationTemplateChangeRequest,
        properties: Map<String, Any>
    ): NotificationTemplateData {
        val scheme =
            properties[SCHEME_REFERENCE] as? NotificationScheme ?: throw InvalidRequestException("Cannot find scheme")
        return NotificationTemplateData.create(scheme, request, timeProvider.now())
    }

    override fun updateData(
        data: NotificationTemplateData,
        request: NotificationTemplateChangeRequest
    ): NotificationTemplateData {
        return data.update(request, timeProvider.now())
    }

    override fun isValid(request: NotificationTemplateChangeRequest) {
        if (request.subject.isBlank()) throw InvalidRequestException("Subject cannot be blank.")
    }

    override fun findByScheme(scheme: NotificationScheme, pageable: Pageable): Page<NotificationTemplate> {
        return repository.findBySchemeId(scheme.id, pageable).map { it.convert() }
    }

}

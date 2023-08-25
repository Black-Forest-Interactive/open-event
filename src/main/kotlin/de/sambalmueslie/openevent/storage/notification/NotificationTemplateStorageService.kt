package de.sambalmueslie.openevent.storage.notification


import de.sambalmueslie.openevent.core.model.NotificationTemplate
import de.sambalmueslie.openevent.core.model.NotificationTemplateChangeRequest
import de.sambalmueslie.openevent.core.model.NotificationType
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
import java.util.*

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
        private const val TYPE_REFERENCE = "type"
    }


    override fun create(type: NotificationType, request: NotificationTemplateChangeRequest): NotificationTemplate {
        return create(request, mapOf(Pair(TYPE_REFERENCE, type)))
    }

    override fun createData(
        request: NotificationTemplateChangeRequest,
        properties: Map<String, Any>
    ): NotificationTemplateData {
        val scheme = properties[TYPE_REFERENCE] as? NotificationType
            ?: throw InvalidRequestException("Cannot find scheme")
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


    override fun findByType(type: NotificationType, pageable: Pageable): Page<NotificationTemplate> {
        return repository.findByTypeId(type.id, pageable).map { it.convert() }
    }

    override fun findByTypeAndLang(type: NotificationType, lang: Locale): NotificationTemplate? {
        return repository.findOneByTypeIdAndLang(type.id, lang.language)?.convert()
    }


}

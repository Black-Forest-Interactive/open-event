package de.sambalmueslie.openevent.infrastructure.audit


import com.fasterxml.jackson.databind.ObjectMapper
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.audit.api.AuditLogEntry
import de.sambalmueslie.openevent.infrastructure.audit.api.AuditLogEntryChangeRequest
import de.sambalmueslie.openevent.infrastructure.audit.api.AuditLogger
import de.sambalmueslie.openevent.infrastructure.audit.db.AuditLogEntryData
import de.sambalmueslie.openevent.infrastructure.audit.db.AuditLogEntryRepository
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
class AuditService(
    private val repository: AuditLogEntryRepository,
    private val timeProvider: TimeProvider,
    private val mapper: ObjectMapper,
    cacheService: CacheService,
) : BaseStorageService<Long, AuditLogEntry, AuditLogEntryChangeRequest, AuditLogEntryData>(
    repository,
    SimpleDataObjectConverter(),
    cacheService,
    AuditLogEntry::class,
    logger
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AuditService::class.java)
    }

    override fun createData(request: AuditLogEntryChangeRequest, properties: Map<String, Any>): AuditLogEntryData {
        return AuditLogEntryData.create(request, mapper)
    }

    override fun updateData(data: AuditLogEntryData, request: AuditLogEntryChangeRequest): AuditLogEntryData {
        return data.update(request, mapper)
    }

    override fun isValid(request: AuditLogEntryChangeRequest) {
        if (request.source.isBlank()) throw InvalidRequestException("Source is not allowed to be blank")
    }


    override fun getAll(pageable: Pageable): Page<AuditLogEntry> {
        return repository.findAllOrderByTimestampDesc(pageable).map { it.convert() }
    }

    fun findByReferenceId(referenceId: String, pageable: Pageable): Page<AuditLogEntry> {
        return repository.findAllByReferenceId(referenceId, pageable).map { it.convert() }
    }

    fun getLogger(source: String): AuditLogger {
        return AuditLoggerImpl(this, timeProvider, source)
    }


}

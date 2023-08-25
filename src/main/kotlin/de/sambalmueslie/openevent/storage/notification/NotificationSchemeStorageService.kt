package de.sambalmueslie.openevent.storage.notification


import de.sambalmueslie.openevent.core.model.*
import de.sambalmueslie.openevent.core.storage.NotificationSchemeStorage
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import de.sambalmueslie.openevent.storage.BaseStorageService
import de.sambalmueslie.openevent.storage.SimpleDataObjectConverter
import de.sambalmueslie.openevent.storage.account.AccountStorageService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class NotificationSchemeStorageService(
    private val repository: NotificationSchemeRepository,
    private val typeRelationRepository: TypeSchemeRelationRepository,
    private val subscriberRelationRepository: SchemeSubscriberRelationRepository,
    private val accountStorage: AccountStorageService,
    cacheService: CacheService,
    private val timeProvider: TimeProvider,
) : BaseStorageService<Long, NotificationScheme, NotificationSchemeChangeRequest, NotificationSchemeData>(
    repository,
    SimpleDataObjectConverter(),
    cacheService,
    NotificationScheme::class,
    logger
), NotificationSchemeStorage {


    companion object {
        private val logger: Logger = LoggerFactory.getLogger(NotificationSchemeStorageService::class.java)
    }

    override fun createData(
        request: NotificationSchemeChangeRequest,
        properties: Map<String, Any>
    ): NotificationSchemeData {
        return NotificationSchemeData.create(request, timeProvider.now())
    }

    override fun updateData(
        data: NotificationSchemeData,
        request: NotificationSchemeChangeRequest
    ): NotificationSchemeData {
        return data.update(request, timeProvider.now())
    }

    override fun isValid(request: NotificationSchemeChangeRequest) {
        if (request.name.isBlank()) throw InvalidRequestException("Name cannot be blank.")
    }

    override fun setEnabled(id: Long, value: PatchRequest<Boolean>): NotificationScheme? {
        return patchData(id) { it.setEnabled(value.value, timeProvider.now()) }
    }

    override fun deleteDependencies(data: NotificationSchemeData) {
        typeRelationRepository.deleteBySchemeId(data.id)
    }

    override fun assign(scheme: NotificationScheme, types: List<NotificationType>) {
        val existing = typeRelationRepository.findBySchemeId(scheme.id).map { it.typeId }.toSet()

        val toAdd = types.filter { !existing.contains(it.id) }
            .map { TypeSchemeRelation(it.id, scheme.id) }
        if (toAdd.isNotEmpty()) typeRelationRepository.saveAll(toAdd)

        val typeIds = types.map { it.id }.toSet()
        val toRemove = existing.filter { !typeIds.contains(it) }
        toRemove.forEach { typeRelationRepository.deleteByTypeId(it) }
    }

    override fun getByType(type: NotificationType, pageable: Pageable): Page<NotificationScheme> {
        val relations = typeRelationRepository.findByTypeId(type.id, pageable)

        val schemeIds = relations.content.map { it.schemeId }.toSet()
        val result = getByIds(schemeIds)
        return Page.of(result, relations.pageable, relations.totalSize)
    }

    override fun getSubscriber(scheme: NotificationScheme, pageable: Pageable): Page<Account> {
        val relations = subscriberRelationRepository.findBySchemeId(scheme.id, pageable)
        val accountIds = relations.content.map { it.accountId }.toSet()
        val result = accountStorage.getByIds(accountIds)
        return Page.of(result, relations.pageable, relations.totalSize)
    }

    override fun getSubscriptionStatus(account: Account): SubscriptionStatus {
        val relations = subscriberRelationRepository.findByAccountId(account.id)
        val subscribedIds = relations.map { it.schemeId }.toSet()

        val schemes = getAll()
        val (subscribed, unsubscribed) = schemes.partition { subscribedIds.contains(it.id) }

        return SubscriptionStatus(subscribed, unsubscribed)
    }

    override fun subscribe(scheme: NotificationScheme, account: Account): SubscriptionStatus {
        if (!subscriberRelationRepository.existsBySchemeIdAndAccountId(scheme.id, account.id)) {
            subscriberRelationRepository.save(SchemeSubscriberRelation(scheme.id, account.id))
        }
        return getSubscriptionStatus(account)

    }

    override fun unsubscribe(scheme: NotificationScheme, account: Account): SubscriptionStatus {
        subscriberRelationRepository.deleteBySchemeIdAndAccountId(scheme.id, account.id)
        return getSubscriptionStatus(account)
    }

}

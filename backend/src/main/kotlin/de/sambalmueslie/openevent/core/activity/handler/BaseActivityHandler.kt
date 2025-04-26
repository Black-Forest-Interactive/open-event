package de.sambalmueslie.openevent.core.activity.handler

import de.sambalmueslie.openevent.common.PageSequence
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.activity.ActivityCrudService
import de.sambalmueslie.openevent.core.activity.ActivitySourceStorage
import de.sambalmueslie.openevent.core.activity.ActivityTypeStorage
import de.sambalmueslie.openevent.core.activity.api.Activity
import de.sambalmueslie.openevent.core.activity.api.ActivityChangeRequest
import de.sambalmueslie.openevent.core.activity.api.ActivitySource
import de.sambalmueslie.openevent.core.activity.api.ActivityType
import de.sambalmueslie.openevent.core.activity.db.ActivitySubscriberRelationService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import org.slf4j.Logger

abstract class BaseActivityHandler(
    private val service: ActivityCrudService,

    private val typeStorage: ActivityTypeStorage,
    private val sourceStorage: ActivitySourceStorage,
    private val subscriberService: ActivitySubscriberRelationService,

    private val logger: Logger
) : ActivityHandler {

    private var source: ActivitySource = ActivitySource.default()
    private var types = mapOf<String, ActivityType>()

    final override fun setup() {
        val sourceKey = getSourceKey()
        source = sourceStorage.findByKey(sourceKey) ?: return logger.error("Cannot find source '$sourceKey' - aborting setup")
        types = typeStorage.findBySource(source).associateBy { it.key }
        if (types.isEmpty()) logger.warn("Cannot find types for source $sourceKey")

        registerListener()
    }

    abstract fun getSourceKey(): String
    abstract fun registerListener()


    protected fun create(actor: Account, request: ActivityChangeRequest, type: String): Activity? {
        val t = types[type]
        if (t == null) {
            logger.warn("[${request.referenceId}] cannot find type '$type' - aborting activity creation")
            return null
        }
        return create(actor, request, source, t)
    }

    private fun create(actor: Account, request: ActivityChangeRequest, source: ActivitySource, type: ActivityType): Activity {
        logger.info("[${request.referenceId}] Create new activity ${request.title} for source ${source.key} and ${type.key} by ${actor.id}")
        return service.create(actor, request, source, type)
    }


    protected fun subscribe(actor: Account, activity: Activity, accountProvider: (pageable: Pageable) -> Page<Account>) {
        val sequence = PageSequence(provider = accountProvider)
        sequence.forEach { page -> subscribe(actor, activity, page.content) }
    }

    protected fun subscribe(actor: Account, activity: Activity, accounts: List<Account>) {
        val accountsById = accounts.filter { it.id != actor.id }.associateBy { it.id }
        val sourceSubscriber = sourceStorage.filterSubscriber(source, accountsById.keys)
        val type = types[activity.type] ?: return logger.error("Cannot find type '${activity.type}' - aborting subscription")
        val subscriberIds = typeStorage.filterSubscriber(type, sourceSubscriber)
        val subscribers = subscriberIds.mapNotNull { accountsById[it] }
        logger.info("[${activity.referenceId}] ${activity.type} found ${subscribers.size} subscriber")
        subscriberService.addAccountSubscriber(activity, subscribers)
    }
}
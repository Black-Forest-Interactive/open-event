package de.sambalmueslie.openevent.core.notification.handler

import de.sambalmueslie.openevent.common.PageSequence
import de.sambalmueslie.openevent.common.PageableSequence
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.activity.ActivityCrudService
import de.sambalmueslie.openevent.core.notification.NotificationEvent
import de.sambalmueslie.openevent.core.notification.NotificationService
import de.sambalmueslie.openevent.core.notification.api.NotificationTypeChangeRequest
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import io.micronaut.data.model.Page
import jakarta.inject.Singleton
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.system.measureTimeMillis

@Singleton
class ActivityNotificationHandler(
    private val activityService: ActivityCrudService,
    private val accountService: AccountCrudService,
    private val service: NotificationService,
    private val timeProvider: TimeProvider
) : NotificationHandler {


    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EventNotificationHandler::class.java)
        const val KEY_ACTIVITY_CREATED = "activity.create"
        const val KEY_ACTIVITY_UPDATED = "activity.update"
        const val KEY_ACTIVITY_DELETED = "activity.delete"
        const val MAX_ACTIVITIES = 100
    }

    override fun getName(): String = EventNotificationHandler::class.java.simpleName

    override fun getTypes(): Set<NotificationTypeChangeRequest> {
        return setOf(
            NotificationTypeChangeRequest(KEY_ACTIVITY_CREATED, "Activity created", ""),
            NotificationTypeChangeRequest(KEY_ACTIVITY_UPDATED, "Activity changed", ""),
            NotificationTypeChangeRequest(KEY_ACTIVITY_DELETED, "Activity deleted", ""),
        )
    }

    private val processing = AtomicBoolean(false)

//    @Scheduled(cron = "* * 22 * * ?")
    fun createDailyActivityNotification() {
        if (processing.get()) return logger.warn("Ignore creation of daily activity notification, cause job hasn't finished yet")
        processing.set(true)
        try {
            logger.info("Starting daily activity notification")
            val timestamp = timeProvider.now().toLocalDate().atStartOfDay()
            val duration = measureTimeMillis {
                val accountSequence = PageSequence { accountService.getAll(it) }
                accountSequence.forEach { page ->
                    createDailyActivityNotification(page, timestamp)
                }
            }
            logger.info("Finished daily activity notification within $duration ms")
        } catch (e: Throwable) {
            logger.error("Failed to start daily activity notification", e)
        }
        processing.set(false)
    }

    private fun createDailyActivityNotification(page: Page<Account>, timestamp: LocalDateTime) {
        logger.info("Starting daily activity notification for page ${page.pageable.number}")
        val duration = measureTimeMillis {
            runBlocking {
                val jobs = mutableListOf<Deferred<Unit>>()
                page.forEach { account ->
                    val job = async { createAccountDailyActivityNotification(account, timestamp) }
                    jobs.add(job)
                }
                jobs.awaitAll()
            }
        }
        logger.info("Finished daily activity notification for page ${page.pageable.number} within $duration ms")
    }

    private fun createAccountDailyActivityNotification(account: Account, timestamp: LocalDateTime) {
        val activities = PageableSequence { activityService.getUnreadForAccount(account, timestamp, it) }
        val result = activities.take(MAX_ACTIVITIES).toList()
        if (result.isEmpty()) return
        val recipient = accountService.getInfo(account)
        service.process(
            NotificationEvent(KEY_ACTIVITY_CREATED, accountService.getSystemAccount(), result),
            setOf(recipient)
        )
    }
}
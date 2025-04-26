package de.sambalmueslie.openevent.core.notification.handler

import de.sambalmueslie.openevent.common.PageSequence
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.account.PreferencesCrudService
import de.sambalmueslie.openevent.core.account.ProfileCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.account.api.Preferences
import de.sambalmueslie.openevent.core.account.api.Profile
import de.sambalmueslie.openevent.core.notification.NotificationEvent
import de.sambalmueslie.openevent.core.notification.NotificationService
import de.sambalmueslie.openevent.core.notification.api.NotificationTypeChangeRequest
import de.sambalmueslie.openevent.core.search.SearchService
import de.sambalmueslie.openevent.core.search.api.EventCreatedSearchRequest
import de.sambalmueslie.openevent.core.search.api.EventSearchResponse
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.system.measureTimeMillis

@Singleton
class NewsletterNotificationHandler(
    private val searchService: SearchService,
    private val accountService: AccountCrudService,
    private val profilesService: ProfileCrudService,
    private val preferencesService: PreferencesCrudService,
    private val service: NotificationService,
    private val timeProvider: TimeProvider
) : NotificationHandler {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EventNotificationHandler::class.java)
        const val KEY_EVENT_NEWSLETTER = "newsletter.event"
        const val MAX_RESULT = 1000
    }

    override fun getName(): String = EventNotificationHandler::class.java.simpleName

    override fun getTypes(): Set<NotificationTypeChangeRequest> {
        return setOf(
            NotificationTypeChangeRequest(KEY_EVENT_NEWSLETTER, "Event Newsletter", "")
        )
    }

    private val processing = AtomicBoolean(false)

    @Scheduled(cron = "0 0 2 * * ?")
    fun createDailyNewsletterNotification() {
        if (processing.get()) return logger.warn("Ignore creation of daily newsletter notification, cause job hasn't finished yet")
        processing.set(true)
        val actor = accountService.getSystemAccount()
        try {
            logger.info("Starting daily newsletter notification")
            val timestamp = timeProvider.now().toLocalDate().minusDays(1)
            val duration = measureTimeMillis {
                val request = EventCreatedSearchRequest(timestamp.atStartOfDay(), timestamp.plusDays(1).atStartOfDay(), true, true)
                val result = searchService.searchEvents(actor, request, Pageable.from(0, MAX_RESULT))
                if (result.result.isEmpty) {
                    logger.info("No events found for request $request, skipping creation")
                    return@measureTimeMillis
                }

                val accountSequence = PageSequence { accountService.getAll(it) }
                accountSequence.forEach { page ->
                    createAccountsDailyNewsletterNotification(actor, timestamp, result, page)
                }
            }
            logger.info("Finished daily newsletter notification within $duration ms")
        } catch (e: Throwable) {
            logger.error("Failed to start daily newsletter notification", e)
        }
        processing.set(false)
    }

    private fun createAccountsDailyNewsletterNotification(actor: Account, timestamp: LocalDate, result: EventSearchResponse, page: Page<Account>) {
        logger.info("Starting daily newsletter notification for page ${page.pageable.number}")
        val duration = measureTimeMillis {

            val validAccounts = page.content.filter { isValidAccount(timestamp, it) }.associateBy { it.id }
            logger.debug("[${page.pageable.number}] Found ${validAccounts.size} valid accounts out of ${page.content.size}")
            val preferences = preferencesService.getByIds(validAccounts.keys).associateBy { it.id }
            val profiles = profilesService.getByIds(validAccounts.keys).associateBy { it.id }

            val recipients = validAccounts.mapNotNull { (accountId, account) -> getRecipient(actor, result, account, preferences[accountId], profiles[accountId]) }
            logger.debug("[${page.pageable.number}] Found ${recipients.size} recipients")
            if (recipients.isEmpty()) return@measureTimeMillis

            service.process(NotificationEvent(KEY_EVENT_NEWSLETTER, actor, result.result.content), recipients.toSet())
        }
        logger.info("Finished daily newsletter notification for page ${page.pageable.number} within $duration ms")
    }

    private fun isValidAccount(timestamp: LocalDate, account: Account): Boolean {
        if (account.serviceAccount) return false
        val lastLoginDate = account.lastLoginDate ?: return false
        return lastLoginDate.toLocalDate().isAfter(timestamp.minusYears(1))
    }

    private fun getRecipient(actor: Account, result: EventSearchResponse, account: Account, preferences: Preferences?, profile: Profile?): AccountInfo? {
        if (preferences == null) {
            logger.error("Cannot find preferences for account ${account.id}")
            return null
        }
        if (profile == null) {
            logger.error("Cannot find profile for account ${account.id}")
            return null
        }
        if (!preferences.emailNotificationsPreferences.enabled) {
            logger.debug("Ignore account ${account.id} cause email notification is disabled")
            return null
        }
        return AccountInfo.create(account, profile)
    }


}
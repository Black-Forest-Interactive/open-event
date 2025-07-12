package de.sambalmueslie.openevent.core.share

import de.sambalmueslie.openevent.common.BaseCrudService
import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.share.api.Share
import de.sambalmueslie.openevent.core.share.api.ShareChangeRequest
import de.sambalmueslie.openevent.core.share.api.ShareInfo
import de.sambalmueslie.openevent.core.share.db.ShareStorage
import de.sambalmueslie.openevent.infrastructure.settings.SettingsService
import io.micronaut.http.uri.UriBuilder
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class ShareCrudService(
    private val storage: ShareStorage,
    private val accountService: AccountCrudService,
    private val settings: SettingsService,
) : BaseCrudService<String, Share, ShareChangeRequest, ShareChangeListener>(storage) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ShareCrudService::class.java)
    }

    fun create(actor: Account, event: Event, request: ShareChangeRequest): Share {
        val result = storage.create(event, request)
        notifyCreated(actor, result)
        return result
    }

    fun setEnabled(actor: Account, event: Event, value: PatchRequest<Boolean>): Share? {
        val share = storage.findByEvent(event)
        if (share != null) {
            val result = storage.setEnabled(share.id, value) ?: return null
            notify { it.enabledChanged(actor, result) }
            return result
        } else {
            return create(actor, event, ShareChangeRequest(value.value))
        }
    }

    override fun isValid(request: ShareChangeRequest) {
        // intentionally left empty
    }

    fun getInfo(event: Event, account: Account?): ShareInfo? {
        val info = account?.let { accountService.getInfo(it) }
        return getInfo(event, info)
    }

    fun getInfo(event: Event, account: AccountInfo?): ShareInfo? {
        val share = storage.findByEvent(event) ?: return null
        return share.toInfo(account ?: event.owner)
    }

    fun findInfosByEventIds(eventIds: Set<Long>, account: Account?): List<ShareInfo> {
        val info = account?.let { accountService.getInfo(it) }
        val shares = storage.findByEventIds(eventIds)
        return shares.mapNotNull { it.toInfo(info) }
    }

    private fun Share.toInfo(account: AccountInfo?): ShareInfo? {
        val language = account?.language ?: settings.getLanguage()
        val url = UriBuilder.of(settings.getShareUrl())
            .path("event")
            .path(id)
            .queryParam("lang", language)
            .toString()
        return ShareInfo(this, url)
    }

    fun deleteByEvent(actor: Account, event: Event): Share? {
        val existing = storage.findByEvent(event) ?: return null
        storage.delete(existing.id)
        notifyDeleted(actor, existing)
        return existing
    }

}



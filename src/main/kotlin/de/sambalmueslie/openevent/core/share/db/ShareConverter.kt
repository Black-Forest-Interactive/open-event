package de.sambalmueslie.openevent.core.share.db

import de.sambalmueslie.openevent.common.DataObjectConverter
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.account.db.AccountStorageService
import de.sambalmueslie.openevent.core.share.api.Share
import de.sambalmueslie.openevent.error.InconsistentDataException
import de.sambalmueslie.openevent.infrastructure.settings.SettingsService
import io.micronaut.data.model.Page
import jakarta.inject.Singleton

@Singleton
class ShareConverter(
    private val accountService: AccountStorageService,
    private val settings: SettingsService,
) : DataObjectConverter<Share, ShareData> {
    override fun convert(obj: ShareData): Share {
        return convert(obj, accountService.getInfo(obj.ownerId))
    }

    override fun convert(objs: List<ShareData>): List<Share> {
        val ownerIds = objs.map { it.ownerId }.toSet()
        val author = accountService.getInfoByIds(ownerIds).associateBy { it.id }
        return objs.map { convert(it, author[it.ownerId]) }
    }

    override fun convert(page: Page<ShareData>): Page<Share> {
        val ownerIds = page.content.map { it.ownerId }.toSet()
        val author = accountService.getInfoByIds(ownerIds).associateBy { it.id }
        return page.map { convert(it, author[it.ownerId]) }
    }

    private fun convert(data: ShareData, owner: AccountInfo?): Share {
        if (owner == null) throw InconsistentDataException("Cannot find owner for share")
        return data.convert(owner, settings.getShareUrl())
    }
}
package de.sambalmueslie.openevent.core.share.db

import de.sambalmueslie.openevent.common.DataObjectConverter
import de.sambalmueslie.openevent.core.account.db.AccountStorageService
import de.sambalmueslie.openevent.core.share.api.Share
import io.micronaut.data.model.Page
import jakarta.inject.Singleton

@Singleton
class ShareConverter(
    private val accountService: AccountStorageService
) : DataObjectConverter<Share, ShareData> {
    override fun convert(obj: ShareData): Share {
        return obj.convert()
    }

    override fun convert(objs: List<ShareData>): List<Share> {
        return objs.map { convert(it) }
    }

    override fun convert(page: Page<ShareData>): Page<Share> {
        return page.map { convert(it) }
    }

}
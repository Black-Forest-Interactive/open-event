package de.sambalmueslie.openevent.core.announcement.db


import de.sambalmueslie.openevent.common.DataObjectConverter
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.db.AccountStorageService
import de.sambalmueslie.openevent.core.announcement.api.Announcement
import de.sambalmueslie.openevent.error.InconsistentDataException
import io.micronaut.data.model.Page
import jakarta.inject.Singleton

@Singleton
class AnnouncementConverter(
    private val accountService: AccountStorageService,
) : DataObjectConverter<Announcement, AnnouncementData> {

    override fun convert(obj: AnnouncementData): Announcement {
        return convert(obj, accountService.get(obj.authorId))
    }

    override fun convert(objs: List<AnnouncementData>): List<Announcement> {
        val authorIds = objs.map { it.authorId }.toSet()
        val author = accountService.getByIds(authorIds).associateBy { it.id }
        return objs.map { convert(it, author[it.authorId]) }
    }

    override fun convert(page: Page<AnnouncementData>): Page<Announcement> {
        val authorIds = page.content.map { it.authorId }.toSet()
        val author = accountService.getByIds(authorIds).associateBy { it.id }
        return page.map { convert(it, author[it.authorId]) }
    }

    private fun convert(data: AnnouncementData, author: Account?): Announcement {
        if (author == null) throw InconsistentDataException("Cannot find author for announcement")
        return data.convert(author)
    }
}

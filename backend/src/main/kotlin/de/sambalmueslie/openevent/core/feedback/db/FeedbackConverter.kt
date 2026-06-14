package de.sambalmueslie.openevent.core.feedback.db

import de.sambalmueslie.openevent.common.DataObjectConverter
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.db.AccountStorageService
import de.sambalmueslie.openevent.core.feedback.api.Feedback
import de.sambalmueslie.openevent.error.InconsistentDataException
import io.micronaut.data.model.Page
import jakarta.inject.Singleton

@Singleton
class FeedbackConverter(
    private val accountService: AccountStorageService,
) : DataObjectConverter<Feedback, FeedbackData> {
    override fun convert(obj: FeedbackData): Feedback {
        return convert(obj, accountService.get(obj.accountId))
    }

    override fun convert(objs: List<FeedbackData>): List<Feedback> {
        val authorIds = objs.map { it.accountId }.toSet()
        val author = accountService.getByIds(authorIds).associateBy { it.id }
        return objs.map { convert(it, author[it.accountId]) }
    }

    override fun convert(page: Page<FeedbackData>): Page<Feedback> {
        val authorIds = page.content.map { it.accountId }.toSet()
        val author = accountService.getByIds(authorIds).associateBy { it.id }
        return page.map { convert(it, author[it.accountId]) }
    }

    private fun convert(data: FeedbackData, account: Account?): Feedback {
        if (account == null) throw InconsistentDataException("Cannot find account for feedback")
        return data.convert(account)
    }
}
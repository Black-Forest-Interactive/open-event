package de.sambalmueslie.openevent.core.issue.db

import de.sambalmueslie.openevent.common.DataObjectConverter
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.db.AccountStorageService
import de.sambalmueslie.openevent.core.issue.api.Issue
import de.sambalmueslie.openevent.error.InconsistentDataException
import io.micronaut.data.model.Page
import jakarta.inject.Singleton

@Singleton
class IssueConverter(
    private val accountService: AccountStorageService,
) : DataObjectConverter<Issue, IssueData> {
    override fun convert(obj: IssueData): Issue {
        return convert(obj, accountService.get(obj.accountId))
    }

    override fun convert(objs: List<IssueData>): List<Issue> {
        val authorIds = objs.map { it.accountId }.toSet()
        val author = accountService.getByIds(authorIds).associateBy { it.id }
        return objs.map { convert(it, author[it.accountId]) }
    }

    override fun convert(page: Page<IssueData>): Page<Issue> {
        val authorIds = page.content.map { it.accountId }.toSet()
        val author = accountService.getByIds(authorIds).associateBy { it.id }
        return page.map { convert(it, author[it.accountId]) }
    }

    private fun convert(data: IssueData, account: Account?): Issue {
        if (account == null) throw InconsistentDataException("Cannot find account for issue")
        return data.convert(account)
    }
}
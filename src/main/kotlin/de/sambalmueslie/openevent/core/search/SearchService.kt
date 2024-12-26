package de.sambalmueslie.openevent.core.search

import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.search.account.AccountSearchOperator
import de.sambalmueslie.openevent.core.search.api.*
import de.sambalmueslie.openevent.core.search.category.CategorySearchOperator
import de.sambalmueslie.openevent.core.search.event.EventSearchOperator
import io.micronaut.context.annotation.Context
import io.micronaut.data.model.Pageable
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Context
class SearchService(
    private val eventOperator: EventSearchOperator,
    private val accountOperator: AccountSearchOperator,
    private val categoryOperator: CategorySearchOperator
) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(SearchService::class.java)
    }

    fun searchEvents(actor: Account, request: EventSearchRequest, pageable: Pageable): EventSearchResponse {
        return eventOperator.search(actor, request, pageable)
    }

    fun setupEvents() {
        return eventOperator.setup()
    }

    fun searchAccounts(actor: Account, request: AccountSearchRequest, pageable: Pageable): AccountSearchResponse {
        return accountOperator.search(actor, request, pageable)
    }

    fun setupAccounts() {
        return accountOperator.setup()
    }

    fun searchCategories(actor: Account, request: CategorySearchRequest, pageable: Pageable): CategorySearchResponse {
        return categoryOperator.search(actor, request, pageable)
    }

    fun setupCategories() {
        return categoryOperator.setup()
    }
}
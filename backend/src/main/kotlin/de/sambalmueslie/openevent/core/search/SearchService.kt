package de.sambalmueslie.openevent.core.search

import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.search.account.AccountSearchOperator
import de.sambalmueslie.openevent.core.search.api.*
import de.sambalmueslie.openevent.core.search.category.CategorySearchOperator
import de.sambalmueslie.openevent.core.search.common.SearchOperatorInfo
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

    private val operators = listOf(eventOperator, accountOperator, categoryOperator).associateBy { it.key() }

    fun searchEvents(actor: Account, request: EventSearchRequest, pageable: Pageable): EventSearchResponse {
        return eventOperator.search(actor, request, pageable)
    }

    fun searchEvents(actor: Account, request: EventCreatedSearchRequest, pageable: Pageable): EventSearchResponse {
        return eventOperator.searchCreated(actor, request, pageable)
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

    fun getInfo() = operators.values.map { it.info() }
    fun getInfo(key: String) = operators[key]?.info()

    fun setup(key: String): SearchOperatorInfo? {
        val operator = operators[key] ?: return null
        operator.setup()
        return operator.info()
    }
}
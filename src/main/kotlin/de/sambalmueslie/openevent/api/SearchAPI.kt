package de.sambalmueslie.openevent.api

import de.sambalmueslie.openevent.core.search.api.*
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication

interface SearchAPI {

    companion object {
        const val PERMISSION_ADMIN = "openevent.search.admin"
    }

    fun searchEvents(
        auth: Authentication,
        request: EventSearchRequest,
        pageable: Pageable
    ): EventSearchResponse

    fun searchAccounts(
        auth: Authentication,
        request: AccountSearchRequest,
        pageable: Pageable
    ): AccountSearchResponse

    fun searchCategories(
        auth: Authentication,
        request: CategorySearchRequest,
        pageable: Pageable
    ): CategorySearchResponse
}
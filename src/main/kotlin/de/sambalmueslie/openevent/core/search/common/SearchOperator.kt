package de.sambalmueslie.openevent.core.search.common

import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.search.api.SearchRequest
import de.sambalmueslie.openevent.core.search.api.SearchResponse
import io.micronaut.data.model.Pageable

interface SearchOperator<T, R : SearchRequest, S : SearchResponse<T>> {
    fun setup()
    fun search(actor: Account, request: R, pageable: Pageable): S
}
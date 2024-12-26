package de.sambalmueslie.openevent.core.search.api

import io.micronaut.data.model.Page

data class AccountSearchResponse(
    override val result: Page<AccountSearchEntry>
) : SearchResponse<AccountSearchEntry>

package de.sambalmueslie.openevent.core.search.api

import de.sambalmueslie.openevent.core.audience.api.Audience
import io.micronaut.data.model.Page

data class AudienceSearchResponse(
    override val result: Page<Audience>
) : SearchResponse<Audience>

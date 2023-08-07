package de.sambalmueslie.openevent.infrastructure.geo

import io.micronaut.http.HttpHeaders.USER_AGENT
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Headers
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client("https://nominatim.openstreetmap.org")
@Headers(
    Header(name = USER_AGENT, value = "Open Event"),
)
interface GeoLocationClient {

    @Get("/search")
    fun search(
        @QueryValue("country") country: String,
        @QueryValue("postalcode") zip: String,
        @QueryValue("street") street: String,
        @QueryValue("city") city: String,
        @QueryValue format: String = "json",
    ): List<QueryResult>

}

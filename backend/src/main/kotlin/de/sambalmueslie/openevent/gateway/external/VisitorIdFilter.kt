package de.sambalmueslie.openevent.gateway.external

import de.sambalmueslie.openevent.config.MetricsConfig
import io.micronaut.http.HttpRequest
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.RequestFilter
import io.micronaut.http.annotation.ResponseFilter
import io.micronaut.http.annotation.ServerFilter
import io.micronaut.http.cookie.Cookie
import io.micronaut.http.cookie.SameSite
import jakarta.inject.Singleton
import java.time.Duration
import java.util.*

private const val VISITOR_ID_COOKIE = "oe_visitor_id"
private const val VISITOR_ID_ATTRIBUTE = "oe.visitorId"
private val VISITOR_ID_COOKIE_MAX_AGE: Duration = Duration.ofDays(365)

@ServerFilter("/api/external/**")
@Singleton
class VisitorIdFilter(private val config: MetricsConfig) {

    @RequestFilter
    fun filterRequest(request: HttpRequest<*>) {
        if (!config.visitorTrackingEnabled) return
        val visitorId = request.cookies.get(VISITOR_ID_COOKIE)?.value ?: UUID.randomUUID().toString()
        request.setAttribute(VISITOR_ID_ATTRIBUTE, visitorId)
    }

    @ResponseFilter
    fun filterResponse(request: HttpRequest<*>, response: MutableHttpResponse<*>) {
        if (!config.visitorTrackingEnabled) return
        if (request.cookies.get(VISITOR_ID_COOKIE) != null) return
        val visitorId = request.getVisitorId() ?: return
        response.cookie(
            Cookie.of(VISITOR_ID_COOKIE, visitorId)
                .maxAge(VISITOR_ID_COOKIE_MAX_AGE)
                .httpOnly(true)
                .sameSite(SameSite.Lax)
                .path("/")
        )
    }
}

fun HttpRequest<*>.getVisitorId(): String = getAttribute(VISITOR_ID_ATTRIBUTE, String::class.java).orElse(null) ?: "unknown"


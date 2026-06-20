package de.sambalmueslie.openevent.testdata

import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.mockk.every
import io.mockk.mockk

/**
 * Mocked [HttpRequest] for services that derive client ip / user agent from the request headers
 * (e.g. feedback, issue). Stubbing X-Real-IP short-circuits the remoteAddress fallback.
 */
object TestHttpRequest {

    fun mock(clientIp: String = "127.0.0.1", userAgent: String = "test-agent"): HttpRequest<*> {
        val headers = mockk<HttpHeaders>()
        every { headers["X-Real-IP"] } returns clientIp
        every { headers["User-Agent"] } returns userAgent

        val request = mockk<HttpRequest<*>>()
        every { request.headers } returns headers
        return request
    }

}

package de.sambalmueslie.openevent


import io.micronaut.http.HttpRequest
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.HttpServerFilter
import io.micronaut.http.filter.ServerFilterChain
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux

@Filter("/**")
class NoCacheFilter : HttpServerFilter {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(NoCacheFilter::class.java)
    }
    init {
        logger.info("No Cache Filter - Up")
    }
    override fun doFilter(request: HttpRequest<*>, chain: ServerFilterChain): Publisher<MutableHttpResponse<*>> {
        return Flux.from(chain.proceed(request)).map { response ->
            if (request.path == "/index.html" || request.path.equals("/")) {
                response.header("Cache-Control", "no-cache, no-store, must-revalidate")
                response.header("Pragma", "no-cache")
                response.header("Expires", "0")
            }
            response
        }
    }


}

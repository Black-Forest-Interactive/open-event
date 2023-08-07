package de.sambalmueslie.openevent


import io.micronaut.http.HttpRequest
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.HttpServerFilter
import io.micronaut.http.filter.ServerFilterChain
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux

@Filter("/**")
class NoCacheFilter : HttpServerFilter {

    override fun doFilter(request: HttpRequest<*>, chain: ServerFilterChain): Publisher<MutableHttpResponse<*>> {
        return Flux.from(chain.proceed(request)).map { response ->
            if (request.path == "/index.html") {
                response.header("Cache-Control", "no-cache, no-store, must-revalidate")
                response.header("Pragma", "no-cache")
                response.header("Expires", "0")
            }
            response
        }
    }


}

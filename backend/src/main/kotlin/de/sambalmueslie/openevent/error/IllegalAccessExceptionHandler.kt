package de.sambalmueslie.openevent.error

import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Produces
@Singleton
@Requires(classes = [IllegalAccessException::class, ExceptionHandler::class])
class IllegalAccessExceptionHandler : ExceptionHandler<IllegalAccessException, HttpResponse<Any>> {

    companion object {
        private val logger = LoggerFactory.getLogger(IllegalAccessExceptionHandler::class.java)
    }

    override fun handle(request: HttpRequest<*>, exception: IllegalAccessException): HttpResponse<Any> {
        logger.warn("Illegal access exception: ${exception.message}")
        return HttpResponse.status(HttpStatus.FORBIDDEN)
    }
}

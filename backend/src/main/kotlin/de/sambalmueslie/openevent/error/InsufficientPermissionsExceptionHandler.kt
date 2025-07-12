package de.sambalmueslie.openevent.error

import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Produces
@Singleton
@Requires(classes = [InsufficientPermissionsException::class, ExceptionHandler::class])
class InsufficientPermissionsExceptionHandler : ExceptionHandler<InsufficientPermissionsException, HttpResponse<Any>> {

    companion object {
        private val logger = LoggerFactory.getLogger(InsufficientPermissionsExceptionHandler::class.java)
    }

    override fun handle(request: HttpRequest<*>, exception: InsufficientPermissionsException): HttpResponse<Any> {
        logger.error("Insufficient permissions exception [${exception.requiredRoles}]", exception)
        return HttpResponse.unauthorized()
    }
}
package de.sambalmueslie.openevent


import io.micronaut.core.io.ResourceResolver
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.types.files.StreamedFile
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.jvm.optionals.getOrNull

@Controller("/")
class IndexController(private val res: ResourceResolver) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(IndexController::class.java)
    }

    init {
        logger.info("Index Controller - Up")
    }

    @Get(value = "/{path:[^\\.]*}", consumes = [MediaType.TEXT_HTML])
    @Produces(MediaType.TEXT_HTML)
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun refresh(path: String): HttpResponse<StreamedFile>? {
        if (logger.isDebugEnabled) logger.debug("Refresh $path")
        val result = res.getResource("classpath:static/index.html").map { StreamedFile(it) }.getOrNull() ?: return null
        return HttpResponse.ok(result).header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
    }

}

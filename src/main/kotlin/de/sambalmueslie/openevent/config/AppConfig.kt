package de.sambalmueslie.openevent.config

import io.micronaut.context.annotation.ConfigurationProperties
import jakarta.validation.constraints.NotBlank
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@ConfigurationProperties("app")
class AppConfig {


    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AppConfig::class.java)
    }


    @NotBlank
    var searchEngine: String = "opensearch"
        set(value) {
            logger.info("Set search engine from '$field' to '$value'")
            field = value
        }

}
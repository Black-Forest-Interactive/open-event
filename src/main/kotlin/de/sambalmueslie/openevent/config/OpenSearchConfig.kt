package de.sambalmueslie.openevent.config

import io.micronaut.context.annotation.ConfigurationProperties
import jakarta.validation.constraints.NotBlank
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@ConfigurationProperties("opensearch")
class OpenSearchConfig {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(OpenSearchConfig::class.java)
    }


    @NotBlank
    var host: String = "localhost"
        set(value) {
            logger.info("Set host from '$field' to '$value'")
            field = value
        }


    var port: Int = 9200
        set(value) {
            logger.info("Set port from '$field' to '$value'")
            field = value
        }


    var https: Boolean = false
        set(value) {
            logger.info("Set https '$field' to '$value'")
            field = value
        }


    @NotBlank
    var user: String = ""
        set(value) {
            logger.info("Set user '$field' to '$value'")
            field = value
        }


    @NotBlank
    var password: String = ""
        set(value) {
            logger.info("Set password from '$field' to '$value'")
            field = value
        }

}
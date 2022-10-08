package de.sambalmueslie.openevent.user.config


import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Requires
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.validation.constraints.NotBlank

@ConfigurationProperties("keycloak")
@Requires(property = "keycloak")
class KeycloakConfig {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(KeycloakConfig::class.java)
    }

    @NotBlank
    var realm: String = ""
        set(value) {
            logger.info("Set realm from '$field' to '$value'")
            field = value
        }

    @NotBlank
    var baseUrl: String = ""
        set(value) {
            logger.info("Set base url from '$field' to '$value'")
            field = value
        }
}

package de.sambalmueslie.openevent.config


import io.micronaut.context.annotation.ConfigurationProperties
import jakarta.validation.constraints.NotBlank
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@ConfigurationProperties("mail")
class MailConfig {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(MailConfig::class.java)
    }


    @NotBlank
    var server: String = ""
        set(value) {
            logger.info("Set server from '$field' to '$value'")
            field = value
        }

    var port: Int = 25
        set(value) {
            logger.info("Set port from '$field' to '$value'")
            field = value
        }

    @NotBlank
    var username: String = ""
        set(value) {
            logger.info("Set username from '$field' to '$value'")
            field = value
        }

    @NotBlank
    var password: String = ""
        set(value) {
            logger.info("Set password")
            field = value
        }

    @NotBlank
    var fromAddress: String = ""
        set(value) {
            logger.info("Set senderAddress from '$field' to '$value'")
            field = value
        }


    @NotBlank
    var replyToAddress: String = ""
        set(value) {
            logger.info("Set replyToAddress from '$field' to '$value'")
            field = value
        }


    @NotBlank
    var defaultAdminAddress: String = ""
        set(value) {
            logger.info("Set defaultAdminAddress from '$field' to '$value'")
            field = value
        }
}

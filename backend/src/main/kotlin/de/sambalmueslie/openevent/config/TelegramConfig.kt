package de.sambalmueslie.openevent.config


import io.micronaut.context.annotation.ConfigurationProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@ConfigurationProperties("telegram")
class TelegramConfig {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(TelegramConfig::class.java)
    }

    var botToken: String = ""
        set(value) {
            logger.info("Set botToken")
            field = value
        }

    var chatId: String = ""
        set(value) {
            logger.info("Set chatId from '$field' to '$value'")
            field = value
        }

    var enabled: Boolean = false
        set(value) {
            logger.info("Set enabled from '$field' to '$value'")
            field = value
        }
}

package de.sambalmueslie.openevent.config

import io.micronaut.context.annotation.ConfigurationProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@ConfigurationProperties("metrics")
class MetricsConfig {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(MetricsConfig::class.java)
    }

    var watermark: Int = 20
        set(value) {
            logger.info("Set metrics watermark from '$field' to '$value'")
            field = value
        }


    var visitorTrackingEnabled: Boolean = false
        set(value) {
            logger.info("Set metrics visitor tracking enabled from '$field' to '$value'")
            field = value
        }

    var traceExpirationDays: Long = 30L
        set(value) {
            logger.info("Set trace expiration days from '$field' to '$value'")
            field = value
        }

    var dailyExpirationDays: Long = 90L
        set(value) {
            logger.info("Set daily expiration days from '$field' to '$value'")
            field = value
        }

    var weeklyExpirationDays: Long = 365L
        set(value) {
            logger.info("Set weekly expiration days from '$field' to '$value'")
            field = value
        }
}
package de.sambalmueslie.openevent

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.micronaut.context.event.BeanCreatedEvent
import io.micronaut.context.event.BeanCreatedEventListener
import io.micronaut.runtime.Micronaut
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class OpenEventApplication {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(OpenEventApplication::class.java)

        @JvmStatic
        fun main(args: Array<String>) {
            Micronaut.build()
                .packages("de.sambalmueslie.openevent")
                .mainClass(OpenEventApplication::class.java)
                .start()
        }
    }

    @Singleton
    internal class ObjectMapperBeanEventListener : BeanCreatedEventListener<ObjectMapper> {
        override fun onCreated(event: BeanCreatedEvent<ObjectMapper>): ObjectMapper {
            val mapper: ObjectMapper = event.bean
            mapper.registerModule(JavaTimeModule())
            mapper.registerModule(
                KotlinModule.Builder()
                    .withReflectionCacheSize(512)
                    .configure(KotlinFeature.NullToEmptyCollection, true)
                    .configure(KotlinFeature.NullToEmptyMap, true)
                    .configure(KotlinFeature.NullIsSameAsDefault, true)
                    .configure(KotlinFeature.StrictNullChecks, false)
                    .build()
            )
            mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS)
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            mapper.disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
            return mapper
        }
    }
}

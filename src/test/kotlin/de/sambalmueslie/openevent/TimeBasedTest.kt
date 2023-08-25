package de.sambalmueslie.openevent


import de.sambalmueslie.openevent.infrastructure.mail.external.MailClient
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import io.micronaut.test.annotation.MockBean
import io.mockk.every
import io.mockk.mockk
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

abstract class TimeBasedTest {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(TimeBasedTest::class.java)
    }

    val timestamp = LocalDateTime.of(2023, 7, 28, 22, 33, 10)
    val provider: TimeProvider = mockk()

    @MockBean(TimeProvider::class)
    fun timeProvider() = provider

    val mailClient = mockk<MailClient>()

    @MockBean(MailClient::class)
    fun mailClient() = mailClient

    init {
        every { provider.now() } returns timestamp
        every { mailClient.send(any(), any(), any()) } returns true
    }

}

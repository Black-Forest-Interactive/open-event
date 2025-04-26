package de.sambalmueslie.openevent


import com.jillesvangurp.ktsearch.KtorRestClient
import com.jillesvangurp.ktsearch.SearchClient
import de.sambalmueslie.openevent.core.search.common.SearchClientFactory
import de.sambalmueslie.openevent.infrastructure.mail.external.MailClient
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import io.micronaut.context.annotation.Replaces
import io.micronaut.test.annotation.MockBean
import io.mockk.every
import io.mockk.mockk
import jakarta.inject.Singleton
import org.opensearch.testcontainers.OpensearchContainer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.DockerImageName
import java.time.LocalDateTime


abstract class TimeBasedTest {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(TimeBasedTest::class.java)

        @Container
        var opensearch: OpensearchContainer<*> = OpensearchContainer(
            DockerImageName.parse("opensearchproject/opensearch:2.13.0")
        )

        @Replaces(SearchClientFactory::class)
        @Singleton
        class MockDependency : SearchClientFactory {
            private val client: SearchClient

            init {
                opensearch.start()
                client = SearchClient(
                    KtorRestClient(
                        host = opensearch.host,
                        port = opensearch.firstMappedPort,
                        https = false,
                        user = opensearch.username,
                        password = opensearch.password,
                        logging = false
                    )
                )
            }

            override fun getClient(): SearchClient {
                return client
            }


        }

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

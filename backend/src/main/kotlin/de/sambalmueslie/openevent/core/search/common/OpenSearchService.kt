package de.sambalmueslie.openevent.core.search.common

import com.jillesvangurp.ktsearch.KtorRestClient
import com.jillesvangurp.ktsearch.SearchClient
import de.sambalmueslie.openevent.config.OpenSearchConfig
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class OpenSearchService(
    private val config: OpenSearchConfig
) : SearchClientFactory {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(OpenSearchService::class.java)
    }

    override fun getClient(): SearchClient {
        return SearchClient(
            KtorRestClient(
                host = config.host,
                port = config.port,
                https = config.https,
                user = config.user,
                password = config.password,
                logging = false
            )
        )
    }
}
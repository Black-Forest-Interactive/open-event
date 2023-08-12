package de.sambalmueslie.openevent.infrastructure.search


import de.sambalmueslie.openevent.config.SolrConfig
import jakarta.inject.Singleton
import org.apache.solr.client.solrj.impl.Http2SolrClient
import org.apache.solr.client.solrj.impl.XMLResponseParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory


@Singleton
class SolrSearchService(
    private val config: SolrConfig
) : SearchService {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(SolrSearchService::class.java)
    }

    override fun <T> getClient(name: String): SearchClient<T> {
        val client: Http2SolrClient = Http2SolrClient.Builder("${config.baseUrl}/solr/$name")
            .withResponseParser(XMLResponseParser())
            .build()
        logger.info("Create solr search client $name")
        return SolrSearchClient(name, client)
    }


}

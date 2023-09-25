package de.sambalmueslie.openevent.infrastructure.search


import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import org.apache.solr.common.SolrDocument
import org.apache.solr.common.SolrInputDocument
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class NopSearchService<T> : SearchClient<T> {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(NopSearchService::class.java)
    }

    override fun delete(key: String) {
        logger.info("delete $key")
    }

    override fun deleteAll() {
        logger.info("delete all")
    }

    override fun get(key: String): SolrDocument? {
        logger.info("get $key")
        return null
    }

    override fun save(input: SolrInputDocument) {
        logger.info("save $input")
    }

    override fun search(query: String, pageable: Pageable): Page<SolrDocument> {
        logger.info("search $query")
        return Page.empty()
    }


}

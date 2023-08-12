package de.sambalmueslie.openevent.infrastructure.search


import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import org.apache.poi.ss.formula.functions.T
import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.impl.Http2SolrClient
import org.apache.solr.common.SolrDocument
import org.apache.solr.common.SolrInputDocument
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class SolrSearchClient<T>(
    private val name: String,
    private val client: Http2SolrClient
) : SearchClient<T> {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(SolrSearchClient::class.java)
    }

    override fun delete(key: String) {
        client.deleteById(key)
        client.commit()
    }

    override fun deleteAll() {
        client.deleteByQuery("*:*")
        client.commit()
    }

    override fun get(key: String): SolrDocument? {
        return client.getById(key)
    }

    override fun save(input: SolrInputDocument) {
        client.add(input)
        client.commit()
    }

    override fun search(query: String, pageable: Pageable): Page<SolrDocument> {
        val q = SolrQuery()
        q.set("q", query)
        q.set("start", pageable.offset.toString())
        q.set("rows", pageable.size.toString())

        val response = client.query(q)


        val result = response.results
        val totalSize = result.numFound

        return Page.of(result, pageable, totalSize)
    }

}

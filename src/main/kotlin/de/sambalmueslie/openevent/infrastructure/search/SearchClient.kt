package de.sambalmueslie.openevent.infrastructure.search

import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import org.apache.solr.common.SolrDocument
import org.apache.solr.common.SolrInputDocument

interface SearchClient<T> {
    fun delete(key: String)
    fun deleteAll()

    fun get(key: String): SolrDocument?
    fun save(input: SolrInputDocument)

    fun search(query: String, pageable: Pageable): Page<SolrDocument>
}

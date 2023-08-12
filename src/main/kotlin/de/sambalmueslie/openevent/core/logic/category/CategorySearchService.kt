package de.sambalmueslie.openevent.core.logic.category


import de.sambalmueslie.openevent.core.model.Category
import de.sambalmueslie.openevent.infrastructure.search.BaseSearchService
import de.sambalmueslie.openevent.infrastructure.search.SearchService
import jakarta.inject.Singleton
import org.apache.solr.common.SolrInputDocument
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
open class CategorySearchService(
    private val service: CategoryCrudService,
    searchService: SearchService,
) : BaseSearchService<Long, Category>(service, searchService, "oe.category", logger) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(CategorySearchService::class.java)
    }

    override fun convert(obj: Category): SolrInputDocument {
        val input = SolrInputDocument()
        input.addField("id", obj.id.toString())
        input.addField("name", obj.name)
        return input
    }

    override fun buildSolrQuery(query: String): String {
        return "name:*$query*"
    }

    override fun convertId(id: String): Long? {
        return id.toLongOrNull()
    }

}

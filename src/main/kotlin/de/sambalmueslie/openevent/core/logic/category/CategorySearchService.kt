package de.sambalmueslie.openevent.core.logic.category


import de.sambalmueslie.openevent.core.model.Account
import de.sambalmueslie.openevent.core.model.Category
import de.sambalmueslie.openevent.infrastructure.search.BaseSearchService
import de.sambalmueslie.openevent.infrastructure.search.SearchService
import io.micronaut.context.annotation.Context
import org.apache.solr.common.SolrInputDocument
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Context
open class CategorySearchService(
    private val service: CategoryCrudService,
    searchService: SearchService,
) : BaseSearchService<Long, Category>(service, searchService, "oe.category", logger), CategoryChangeListener {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(CategorySearchService::class.java)
    }
    init {
        service.register(this)
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
    override fun handleCreated(actor: Account, obj: Category) {
        super.handleChanged(obj)
    }

    override fun handleUpdated(actor: Account,obj: Category) {
        super.handleChanged(obj)
    }

    override fun handleDeleted(actor: Account,obj: Category) {
        super.handleRemoved(obj)
    }
}

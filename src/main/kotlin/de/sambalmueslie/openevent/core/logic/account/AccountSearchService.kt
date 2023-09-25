package de.sambalmueslie.openevent.core.logic.account


import de.sambalmueslie.openevent.core.model.Account
import de.sambalmueslie.openevent.infrastructure.search.BaseSearchService
import de.sambalmueslie.openevent.infrastructure.search.SearchService
import io.micronaut.context.annotation.Context
import org.apache.solr.common.SolrInputDocument
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Context
open class AccountSearchService(
    private val service: AccountCrudService,
    searchService: SearchService,
) : BaseSearchService<Long, Account>(service, searchService, "oe.account", logger), AccountChangeListener {


    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AccountSearchService::class.java)
    }
    init {
        service.register(this)
    }
    override fun convert(obj: Account): SolrInputDocument {
        val input = SolrInputDocument()
        input.addField("id", obj.id.toString())
        input.addField("name", obj.name)
        input.addField("firstName", obj.firstName)
        input.addField("lastName", obj.lastName)
        input.addField("email", obj.email)
        return input
    }

    override fun buildSolrQuery(query: String): String {
        return "name:*$query* OR firstName:*$query* OR lastName:*$query* OR email:*$query*"
    }

    override fun convertId(id: String): Long? {
        return id.toLongOrNull()
    }

    override fun handleCreated(actor: Account,obj: Account) {
        super.handleChanged(obj)
    }

    override fun handleUpdated(actor: Account,obj: Account) {
        super.handleChanged(obj)
    }

    override fun handleDeleted(actor: Account,obj: Account) {
        super.handleRemoved(obj)
    }

}

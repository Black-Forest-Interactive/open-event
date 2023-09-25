package de.sambalmueslie.openevent.core.logic.event


import de.sambalmueslie.openevent.core.model.Account
import de.sambalmueslie.openevent.core.model.Event
import de.sambalmueslie.openevent.core.model.EventInfo
import de.sambalmueslie.openevent.infrastructure.search.BaseSearchService
import de.sambalmueslie.openevent.infrastructure.search.SearchService
import io.micronaut.context.annotation.Context
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import org.apache.solr.common.SolrInputDocument
import org.jsoup.Jsoup
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Context
open class EventSearchService(
    private val service: EventCrudService,
    searchService: SearchService,
) : BaseSearchService<Long, Event>(service, searchService, "oe.event", logger), EventChangeListener {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EventSearchService::class.java)
    }

    init {
        service.register(this)
    }

    override fun convert(obj: Event): SolrInputDocument {
        val input = SolrInputDocument()
        input.addField("id", obj.id.toString())
        input.addField("title", obj.title)
        input.addField("content", Jsoup.parse(obj.longText).text())
        input.addField("owner", obj.owner.id)
        input.addField("published", obj.published)
        return input
    }

    override fun buildSolrQuery(query: String): String {
        return "(title:*$query* OR content:*$query*) AND published:true"
    }

    override fun convertId(id: String): Long? {
        return id.toLongOrNull()
    }

    fun searchInfo(query: String, pageable: Pageable): Page<EventInfo> {
        val result = search(query, pageable)
        return service.convertInfo(result)
    }

    override fun handleCreated(actor: Account, obj: Event) {
        super.handleChanged(obj)
    }

    override fun handleUpdated(actor: Account, obj: Event) {
        super.handleChanged(obj)
    }

    override fun publishedChanged(actor: Account, event: Event) {
        super.handleChanged(event)
    }

    override fun handleDeleted(actor: Account, obj: Event) {
        super.handleRemoved(obj)
    }

}

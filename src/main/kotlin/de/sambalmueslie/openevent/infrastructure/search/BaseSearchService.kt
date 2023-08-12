package de.sambalmueslie.openevent.infrastructure.search


import de.sambalmueslie.openevent.core.BusinessObject
import de.sambalmueslie.openevent.core.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.CrudService
import de.sambalmueslie.openevent.storage.util.PageableSequence
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.scheduling.annotation.Async
import org.apache.solr.common.SolrInputDocument
import org.slf4j.Logger
import kotlin.system.measureTimeMillis

abstract class BaseSearchService<T, O : BusinessObject<T>>(
    private val service: CrudService<T, O, *>,
    private val searchService: SearchService,
    private val name: String,
    private val logger: Logger
) {


    init {
        service.register(object : BusinessObjectChangeListener<T, O> {
            override fun handleCreated(obj: O) {
                handleChanged(obj)
            }

            override fun handleUpdated(obj: O) {
                handleChanged(obj)
            }

            override fun handleDeleted(obj: O) {
                handleRemoved(obj)
            }
        })
    }

    protected val client = searchService.getClient<Long>(name)
    private fun handleChanged(obj: O) {
        val input = convert(obj)
        client.save(input)
    }

    private fun handleRemoved(obj: O) {
        client.delete(obj.id.toString())
    }

    protected abstract fun convert(obj: O): SolrInputDocument

    @Async
    open fun createIndex() {
        val duration = measureTimeMillis {
            client.deleteAll()
            val sequence = PageableSequence { service.getAll(it) }
            sequence.forEach { handleChanged(it) }
        }
        logger.info("Index build within $duration ms.")
    }

    fun search(query: String, pageable: Pageable): Page<O> {
        val solrQuery = buildSolrQuery(query)
        val result = client.search(solrQuery, pageable)
        val ids = result.mapNotNull { it.getFieldValue("id") as? String }
            .mapNotNull { convertId(it) }
            .toSet()

        val objs = service.getByIds(ids)
        return Page.of(objs, result.pageable, result.totalSize)
    }

    abstract fun convertId(id: String): T?

    abstract fun buildSolrQuery(query: String): String
}

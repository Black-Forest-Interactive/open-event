package de.sambalmueslie.openevent.core.search.category

import com.jillesvangurp.ktsearch.SearchResponse
import com.jillesvangurp.ktsearch.ids
import com.jillesvangurp.ktsearch.total
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.category.CategoryChangeListener
import de.sambalmueslie.openevent.core.category.CategoryCrudService
import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.core.search.api.CategorySearchRequest
import de.sambalmueslie.openevent.core.search.api.CategorySearchResponse
import de.sambalmueslie.openevent.core.search.api.SearchRequest
import de.sambalmueslie.openevent.core.search.common.BaseOpenSearchOperator
import de.sambalmueslie.openevent.core.search.common.SearchClientFactory
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
open class CategorySearchOperator(
    private val service: CategoryCrudService,

    private val fieldMapping: CategoryFieldMappingProvider,
    private val queryBuilder: CategorySearchQueryBuilder,

    openSearch: SearchClientFactory
) : BaseOpenSearchOperator<Category, CategorySearchRequest, CategorySearchResponse>(openSearch, "oe.category", logger) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(CategorySearchOperator::class.java)
    }

    init {
        service.register(object : CategoryChangeListener {
            override fun handleCreated(actor: Account, obj: Category) {
                handleChanged(obj)
            }

            override fun handleUpdated(actor: Account, obj: Category) {
                handleChanged(obj)
            }

            override fun handleDeleted(actor: Account, obj: Category) {
                deleteDocument(obj.id.toString())
            }
        })
    }

    private fun handleChanged(category: Category) {
        val data = convert(category)
        updateDocument(data)
    }


    override fun getFieldMappingProvider() = fieldMapping
    override fun getSearchQueryBuilder() = queryBuilder

    override fun initialLoadPage(pageable: Pageable): Page<Pair<String, String>> {
        val page = service.getAll(pageable)
        return page.map { convert(it) }
    }

    private fun convert(obj: Category): Pair<String, String> {
        val input = CategorySearchEntryData.create(obj)
        return Pair(input.id, mapper.writeValueAsString(input))
    }

    override fun processSearchResponse(
        actor: Account,
        request: SearchRequest,
        response: SearchResponse,
        pageable: Pageable
    ): CategorySearchResponse {
        val ids = response.ids.map { it.toLong() }.toSet()
        val result = service.getByIds(ids)

        return CategorySearchResponse(Page.of(result, pageable, response.total))
    }


}
package de.sambalmueslie.openevent.core.search.audience

import com.jillesvangurp.ktsearch.SearchResponse
import com.jillesvangurp.ktsearch.ids
import com.jillesvangurp.ktsearch.total
import de.sambalmueslie.openevent.config.OpenSearchConfig
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.audience.AudienceChangeListener
import de.sambalmueslie.openevent.core.audience.AudienceCrudService
import de.sambalmueslie.openevent.core.audience.api.Audience
import de.sambalmueslie.openevent.core.search.api.AudienceSearchRequest
import de.sambalmueslie.openevent.core.search.api.AudienceSearchResponse
import de.sambalmueslie.openevent.core.search.api.SearchRequest
import de.sambalmueslie.openevent.core.search.common.BaseOpenSearchOperator
import de.sambalmueslie.openevent.core.search.common.SearchClientFactory
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
open class AudienceSearchOperator(
    private val service: AudienceCrudService,

    private val fieldMapping: AudienceFieldMappingProvider,
    private val queryBuilder: AudienceSearchQueryBuilder,
    private val config: OpenSearchConfig,
    openSearch: SearchClientFactory
) : BaseOpenSearchOperator<Audience, AudienceSearchRequest, AudienceSearchResponse>(openSearch, "audience", config, logger) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AudienceSearchOperator::class.java)
    }

    init {
        service.register(object : AudienceChangeListener {
            override fun handleCreated(actor: Account, obj: Audience) {
                handleChanged(obj)
            }

            override fun handleUpdated(actor: Account, obj: Audience) {
                handleChanged(obj)
            }

            override fun handleDeleted(actor: Account, obj: Audience) {
                deleteDocument(obj.id.toString())
            }
        })
    }

    private fun handleChanged(audience: Audience) {
        val data = convert(audience)
        updateDocument(data)
    }


    override fun getFieldMappingProvider() = fieldMapping
    override fun getSearchQueryBuilder() = queryBuilder

    override fun initialLoadPage(pageable: Pageable): Page<Pair<String, String>> {
        val page = service.getAll(pageable)
        return page.map { convert(it) }
    }

    private fun convert(obj: Audience): Pair<String, String> {
        val input = AudienceSearchEntryData.create(obj)
        return Pair(input.id, mapper.writeValueAsString(input))
    }

    override fun processSearchResponse(
        actor: Account,
        request: SearchRequest,
        response: SearchResponse,
        pageable: Pageable
    ): AudienceSearchResponse {
        val ids = response.ids.map { it.toLong() }.toSet()
        val result = service.getByIds(ids)

        return AudienceSearchResponse(Page.of(result, pageable, response.total))
    }


}
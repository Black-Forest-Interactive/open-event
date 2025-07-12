package de.sambalmueslie.openevent.core.search.common


import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.jillesvangurp.ktsearch.*
import de.sambalmueslie.openevent.common.PageSequence
import de.sambalmueslie.openevent.config.OpenSearchConfig
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.search.api.SearchRequest
import de.sambalmueslie.openevent.core.search.api.SearchResponse
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.scheduling.annotation.Async
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import kotlin.system.measureTimeMillis
import kotlin.time.Duration.Companion.seconds


abstract class BaseOpenSearchOperator<T, R : SearchRequest, S : SearchResponse<T>>(
    openSearch: SearchClientFactory,
    private val name: String,
    config: OpenSearchConfig,
    private val logger: Logger
) : SearchOperator<T, R, S> {

    private val key = "${config.prefix}.$name"

    override fun key(): String {
        return key
    }


    private var status: SearchOperatorStatus = SearchOperatorStatus.UNKNOWN
    private var statsTotal: Long = 0
    private var statsSuccessful: Long = 0
    private var statsFailed: Long = 0

    protected val client = openSearch.getClient()

    protected val mapper = ObjectMapper()
        .registerKotlinModule()
        .registerModule(JavaTimeModule())
        .setSerializationInclusion(JsonInclude.Include.ALWAYS)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)


    init {
        runBlocking {
            try {
                val index = client.getIndex(name)
                status = SearchOperatorStatus.READY
            } catch (e: Exception) {
                status = SearchOperatorStatus.UNKNOWN
            }
        }
    }

    override fun info(): SearchOperatorInfo {
        return SearchOperatorInfo(
            key,
            name,
            status,
            SearchOperatorStats(statsTotal, statsSuccessful, statsFailed)
        )
    }

    @Async
    override fun setup() {
        runBlocking {
            status = SearchOperatorStatus.CREATE_INDEX
            var duration = measureTimeMillis { createIndex() }
            logger.info("[$name] Create index within $duration ms.")
            status = SearchOperatorStatus.INITIAL_LOAD
            duration = measureTimeMillis { initialLoad() }
            logger.info("[$name] Initial load within $duration ms.")
            status = SearchOperatorStatus.READY
        }
    }

    private suspend fun createIndex() {
        val exists = client.exists(key)
        if (exists) client.deleteIndex(key)

        client.createIndex(key) {
            settings {
                replicas = 0
                shards = 3
                refreshInterval = 1.seconds
            }
            mappings(dynamicEnabled = false, getFieldMappingProvider().createMappings())
        }
    }

    abstract fun getFieldMappingProvider(): FieldMappingProvider

    private suspend fun initialLoad() {
        statsTotal = 0
        statsSuccessful = 0
        statsFailed = 0

        val data = PageSequence { initialLoadPage(it) }
        data.forEach { page ->
            statsTotal = page.totalSize
            try {
                client.bulk(target = key) {
                    page.forEach { (id, value) -> index(value, id = id) }
                }
                statsSuccessful += page.content.size
            } catch (e: Exception) {
                statsFailed += page.content.size
            }
        }
    }

    protected fun createDocument(data: Pair<String, String>, blocking: Boolean = true) {
        runBlocking {
            indexDocument(data.first, data.second, blocking)
        }
    }

    protected fun updateDocument(data: Pair<String, String>, blocking: Boolean = true) {
        runBlocking { indexDocument(data.first, data.second, blocking) }
    }

    private suspend fun indexDocument(id: String, value: String, blocking: Boolean) {
        val duration = measureTimeMillis {
            val refresh = if (blocking) Refresh.WaitFor else null
            val existing = try {
                client.getDocument(target = key, id = id).found
            } catch (e: Exception) {
                false
            }
            val type = if (existing) OperationType.Index else OperationType.Create

            client.indexDocument(
                target = key,
                serializedJson = value,
                id = id,
                opType = type,
                refresh = refresh
            )
            statsTotal++
            statsSuccessful++
        }
        logger.info("[$name] index document within $duration ms.")
    }

    private suspend fun indexDocument(id: String, value: String, type: OperationType) {
        val duration = measureTimeMillis {
            client.indexDocument(
                target = key,
                serializedJson = value,
                id = id,
                opType = type
            )
            statsTotal++
            statsSuccessful++
        }
        logger.info("[$name] index document within $duration ms.")
    }

    protected fun deleteDocument(id: String) {
        runBlocking {
            val duration = measureTimeMillis {
                client.deleteDocument(key, id, refresh = Refresh.True)
            }
            logger.info("[$name] delete document within $duration ms.")
        }
    }

    protected abstract fun initialLoadPage(pageable: Pageable): Page<Pair<String, String>>

    override fun search(actor: Account, request: R, pageable: Pageable): S {
        val response = runBlocking {
            client.search(key, block = getSearchQueryBuilder().buildSearchQuery(pageable, request, actor))
        }
        return processSearchResponse(actor, request, response, pageable)
    }

    abstract fun getSearchQueryBuilder(): SearchQueryBuilder<R>

    abstract fun processSearchResponse(
        actor: Account,
        request: SearchRequest,
        response: com.jillesvangurp.ktsearch.SearchResponse,
        pageable: Pageable
    ): S


}
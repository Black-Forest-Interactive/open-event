package de.sambalmueslie.openevent.core.search.account

import com.jillesvangurp.ktsearch.SearchResponse
import com.jillesvangurp.ktsearch.parseHit
import com.jillesvangurp.ktsearch.total
import de.sambalmueslie.openevent.core.account.AccountChangeListener
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.account.ProfileChangeListener
import de.sambalmueslie.openevent.core.account.ProfileCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.Profile
import de.sambalmueslie.openevent.core.search.api.AccountSearchEntry
import de.sambalmueslie.openevent.core.search.api.AccountSearchRequest
import de.sambalmueslie.openevent.core.search.api.AccountSearchResponse
import de.sambalmueslie.openevent.core.search.api.SearchRequest
import de.sambalmueslie.openevent.core.search.common.BaseOpenSearchOperator
import de.sambalmueslie.openevent.core.search.common.SearchClientFactory
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
open class AccountSearchOperator(
    private val service: AccountCrudService,
    private val profileService: ProfileCrudService,

    private val fieldMapping: AccountFieldMappingProvider,
    private val queryBuilder: AccountSearchQueryBuilder,

    openSearch: SearchClientFactory
) : BaseOpenSearchOperator<AccountSearchEntry, AccountSearchRequest, AccountSearchResponse>(
    openSearch,
    "oe.account",
    logger
) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AccountSearchOperator::class.java)
    }

    init {
        service.register(object : AccountChangeListener {
            override fun handleCreated(actor: Account, obj: Account) {
                handleChanged(obj)
            }

            override fun handleUpdated(actor: Account, obj: Account) {
                handleChanged(obj)
            }

            override fun handleDeleted(actor: Account, obj: Account) {
                deleteDocument(obj.id.toString())
            }
        })

        profileService.register(object : ProfileChangeListener {
            override fun handleCreated(actor: Account, obj: Profile) {
                handleChanged(obj)
            }

            override fun handleUpdated(actor: Account, obj: Profile) {
                handleChanged(obj)
            }
        })

    }

    private fun handleChanged(account: Account) {
        val profile = profileService.getForAccount(account)
        val data = convert(account, profile)
        updateDocument(data)
    }

    private fun handleChanged(profile: Profile) {
        val account = service.get(profile.id) ?: return
        val data = convert(account, profile)
        updateDocument(data)
    }

    override fun getFieldMappingProvider() = fieldMapping
    override fun getSearchQueryBuilder() = queryBuilder

    override fun initialLoadPage(pageable: Pageable): Page<Pair<String, String>> {
        val accounts = service.getAll(pageable)
        val accountIds = accounts.map { it.id }.toSet()
        val profiles = profileService.getByIds(accountIds).associateBy { it.id }
        return accounts.map { convert(it, profiles[it.id]) }
    }

    private fun convert(account: Account, profile: Profile?): Pair<String, String> {
        val input = AccountSearchEntryData.create(account, profile)
        return Pair(input.id, mapper.writeValueAsString(input))
    }

    override fun processSearchResponse(
        actor: Account,
        request: SearchRequest,
        response: SearchResponse,
        pageable: Pageable
    ): AccountSearchResponse {
        val data = response.hits?.hits?.map { it.parseHit<AccountSearchEntryData>() }
            ?.map { it.convert() } ?: emptyList()

        return AccountSearchResponse(Page.of(data, pageable, response.total))
    }

}
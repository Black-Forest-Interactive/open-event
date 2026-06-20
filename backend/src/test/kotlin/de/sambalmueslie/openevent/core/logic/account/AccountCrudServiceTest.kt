package de.sambalmueslie.openevent.core.logic.account

import de.sambalmueslie.openevent.TimeBasedTest
import de.sambalmueslie.openevent.core.account.AccountChangeListener
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.account.AccountStorage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.integration.getAuthentication
import de.sambalmueslie.openevent.testdata.AccountTestData
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.*
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest
class AccountCrudServiceTest : TimeBasedTest() {

    @Inject
    lateinit var accountStorage: AccountStorage

    @Inject
    lateinit var service: AccountCrudService

    private fun registerListener(): AccountChangeListener {
        val listener = mockk<AccountChangeListener>()
        service.register(listener)
        every { listener.handleCreated(any(), any()) } just Runs
        every { listener.handleUpdated(any(), any()) } just Runs
        every { listener.handleDeleted(any(), any()) } just Runs
        return listener
    }

    @Test
    fun create() {
        val actor = AccountTestData.createActor(accountStorage)
        val listener = registerListener()

        val request = AccountTestData.accountRequest()
        val account = service.create(actor, request)

        assertTrue(account.id > 0)
        assertEquals(request.name, account.name)
        assertEquals(request.externalId, account.externalId)
        verify { listener.handleCreated(actor, account) }

        // create also provisions a profile and preferences for the new account
        assertNotNull(service.getProfile(account))
        assertNotNull(service.getPreferences(account))
    }

    @Test
    fun get() {
        val actor = AccountTestData.createActor(accountStorage)
        val account = AccountTestData.create(service, actor)

        assertEquals(account, service.get(account.id))
        assertEquals(account, service.getById(account.id))
        assertEquals(listOf(account), service.getByIds(setOf(account.id)))
        assertNull(service.get(-1))
    }

    @Test
    fun getAll() {
        val actor = AccountTestData.createActor(accountStorage)
        val first = AccountTestData.create(service, actor, AccountTestData.accountRequest("first", externalId = "first-id"))
        val second = AccountTestData.create(service, actor, AccountTestData.accountRequest("second", externalId = "second-id"))

        val content = service.getAll(Pageable.from(0)).content
        assertTrue(content.containsAll(listOf(actor, first, second)))
    }

    @Test
    fun update() {
        val actor = AccountTestData.createActor(accountStorage)
        val listener = registerListener()
        val account = AccountTestData.create(service, actor)

        val request = AccountTestData.accountRequest("name-update", "icon-update", "account-id")
        val updated = service.update(actor, account.id, request)

        assertEquals(account.id, updated.id)
        assertEquals(request.name, updated.name)
        assertEquals(request.iconUrl, updated.iconUrl)
        verify { listener.handleUpdated(actor, updated) }
    }

    @Test
    fun delete() {
        val actor = AccountTestData.createActor(accountStorage)
        val listener = registerListener()
        // fully provisioned account — delete must cascade profile + preferences (AccountCrudService.delete)
        val account = AccountTestData.create(service, actor)
        clearMocks(listener, answers = false)

        val deleted = service.delete(actor, account.id)

        assertEquals(account, deleted)
        assertNull(service.get(account.id))
        assertNull(service.getProfile(account))
        assertNull(service.getPreferences(account))
        verify(exactly = 1) { listener.handleDeleted(actor, account) }
        verify(exactly = 0) { listener.handleCreated(any(), any()) }
        verify(exactly = 0) { listener.handleUpdated(any(), any()) }
    }

    @Test
    fun isValidRejectsBlankNameOnCreate() {
        val actor = AccountTestData.createActor(accountStorage)

        assertThrows(InvalidRequestException::class.java) {
            service.create(actor, AccountTestData.accountRequest(name = " "))
        }
    }

    @Test
    fun isValidRejectsBlankNameOnUpdate() {
        val actor = AccountTestData.createActor(accountStorage)
        val account = AccountTestData.create(service, actor)

        assertThrows(InvalidRequestException::class.java) {
            service.update(actor, account.id, AccountTestData.accountRequest(name = " "))
        }
    }

    @Test
    fun findByExternalId() {
        val actor = AccountTestData.createActor(accountStorage)
        val account = AccountTestData.create(service, actor, AccountTestData.accountRequest(externalId = "lookup-id"))

        assertEquals(account, service.findByExternalId("lookup-id"))
        assertNull(service.findByExternalId("unknown"))
    }

    @Test
    fun findByName() {
        val actor = AccountTestData.createActor(accountStorage)
        val account = AccountTestData.create(service, actor, AccountTestData.accountRequest(name = "searchable", externalId = "search-id"))

        val result = service.findByName("searchable", Pageable.from(0)).content
        assertEquals(listOf(account), result)
        assertEquals(emptyList<Account>(), service.findByName("unknown", Pageable.from(0)).content)
    }

    @Test
    fun findByEmail() {
        val actor = AccountTestData.createActor(accountStorage)
        val info = service.setup(actor, AccountTestData.setupRequest())

        val account = service.findByEmail("account@example.com")
        assertNotNull(account)
        assertEquals(info.id, account!!.id)
        assertNull(service.findByEmail("unknown@example.com"))
    }

    @Test
    fun getSystemAccount() {
        val first = service.getSystemAccount()
        val second = service.getSystemAccount()

        assertEquals("system-account", first.externalId)
        assertEquals(first.id, second.id)
    }

    @Test
    fun getInfoAndInfos() {
        val actor = AccountTestData.createActor(accountStorage)
        val account = AccountTestData.create(service, actor)

        val info = service.getInfo(account)
        assertEquals(account.id, info.id)
        assertEquals(account.name, info.name)

        assertTrue(service.getInfos(Pageable.from(0)).content.any { it.id == account.id })
    }

    @Test
    fun validateCreatesAccountThenReusesIt() {
        val auth = getAuthentication("validate@example.com")

        val first = service.validate(auth, "en")
        assertTrue(first.created)
        assertEquals("external-id", first.account.externalId)
        assertEquals("test-username", first.account.name)
        assertEquals("test-first-name", first.profile.firstName)
        assertEquals("test-last-name", first.profile.lastName)
        assertEquals("validate@example.com", first.info.email)

        val second = service.validate(auth, "en")
        assertFalse(second.created)
        assertEquals(first.account.id, second.account.id)
    }

    @Test
    fun getAndFindByAuth() {
        val auth = getAuthentication("auth@example.com")
        val created = service.validate(auth, "en").account

        assertEquals(created, service.get(auth))
        assertEquals(created, service.find(auth))
    }

    @Test
    fun findByAuthReturnsNullWhenMissing() {
        val auth = getAuthentication("missing@example.com")

        assertNull(service.get(auth))
        assertThrows(IllegalArgumentException::class.java) { service.find(auth) }
    }

    @Test
    fun setup() {
        val actor = AccountTestData.createActor(accountStorage)
        val listener = registerListener()

        val info = service.setup(actor, AccountTestData.setupRequest())

        assertEquals("account", info.name)
        assertEquals("account@example.com", info.email)
        assertEquals("first", info.firstName)

        val account = service.findByExternalId("account-id")!!
        assertEquals(info.id, account.id)
        verify { listener.handleCreated(actor, account) }
    }

    @Test
    fun setupUpdate() {
        val actor = AccountTestData.createActor(accountStorage)
        val listener = registerListener()
        val created = service.setup(actor, AccountTestData.setupRequest())

        val request = AccountTestData.setupRequest(
            account = AccountTestData.accountRequest(name = "account-update"),
            profile = AccountTestData.profileRequest(firstName = "first-update"),
        )
        val updated = service.update(actor, created.id, request)

        assertEquals(created.id, updated.id)
        assertEquals("account-update", updated.name)
        assertEquals("first-update", updated.firstName)
        verify { listener.handleUpdated(actor, any()) }
    }

    @Test
    fun notifications() {
        val actor = AccountTestData.createActor(accountStorage)
        val listener = registerListener()

        val account = service.create(actor, AccountTestData.accountRequest())
        verify(exactly = 1) { listener.handleCreated(actor, account) }
        verify(exactly = 0) { listener.handleUpdated(any(), any()) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        val updated = service.update(actor, account.id, AccountTestData.accountRequest(name = "updated"))
        verify(exactly = 1) { listener.handleUpdated(actor, updated) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }
    }

}

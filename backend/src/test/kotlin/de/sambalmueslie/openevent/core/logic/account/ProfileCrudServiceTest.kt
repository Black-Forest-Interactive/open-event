package de.sambalmueslie.openevent.core.logic.account

import de.sambalmueslie.openevent.TimeBasedTest
import de.sambalmueslie.openevent.core.account.AccountStorage
import de.sambalmueslie.openevent.core.account.ProfileChangeListener
import de.sambalmueslie.openevent.core.account.ProfileCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.Profile
import de.sambalmueslie.openevent.testdata.AccountTestData
import de.sambalmueslie.openevent.testdata.ProfileTestData
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.*
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest
class ProfileCrudServiceTest : TimeBasedTest() {

    @Inject
    lateinit var accountStorage: AccountStorage

    @Inject
    lateinit var service: ProfileCrudService

    private fun bareAccount(externalId: String = "account-id"): Account =
        accountStorage.create(AccountTestData.accountRequest(externalId = externalId))

    private fun registerListener(): ProfileChangeListener {
        val listener = mockk<ProfileChangeListener>()
        service.register(listener)
        every { listener.handleCreated(any(), any()) } just Runs
        every { listener.handleUpdated(any(), any()) } just Runs
        every { listener.handleDeleted(any(), any()) } just Runs
        return listener
    }

    @Test
    fun create() {
        val actor = AccountTestData.createActor(accountStorage)
        val account = bareAccount()
        val listener = registerListener()

        val request = ProfileTestData.createRequest()
        val profile = service.create(actor, account, request)

        assertEquals(account.id, profile.id)
        assertEquals(request.email, profile.email)
        assertEquals(request.firstName, profile.firstName)
        assertEquals(request.lastName, profile.lastName)
        assertEquals(request.language, profile.language)
        verify { listener.handleCreated(actor, profile) }
    }

    @Test
    fun get() {
        val actor = AccountTestData.createActor(accountStorage)
        val profile = ProfileTestData.create(service, actor, bareAccount())

        assertEquals(profile, service.get(profile.id))
        assertNull(service.get(-1))
    }

    @Test
    fun getAll() {
        val actor = AccountTestData.createActor(accountStorage)
        val first = ProfileTestData.create(service, actor, bareAccount("a1"), ProfileTestData.createRequest(email = "p1@example.com"))
        val second = ProfileTestData.create(service, actor, bareAccount("a2"), ProfileTestData.createRequest(email = "p2@example.com"))

        assertEquals(setOf(first, second), service.getAll(Pageable.from(0)).content.toSet())
    }

    @Test
    fun update() {
        val actor = AccountTestData.createActor(accountStorage)
        val profile = ProfileTestData.create(service, actor, bareAccount())
        val listener = registerListener()

        val request = ProfileTestData.updateRequest()
        val updated = service.update(actor, profile.id, request)

        assertEquals(profile.id, updated.id)
        assertEquals(request.email, updated.email)
        assertEquals(request.firstName, updated.firstName)
        assertEquals(request.language, updated.language)
        verify { listener.handleUpdated(actor, updated) }
    }

    @Test
    fun delete() {
        val actor = AccountTestData.createActor(accountStorage)
        val profile = ProfileTestData.create(service, actor, bareAccount())
        val listener = registerListener()

        val deleted = service.delete(actor, profile.id)

        assertEquals(profile, deleted)
        assertNull(service.get(profile.id))
        verify(exactly = 1) { listener.handleDeleted(actor, profile) }
        verify(exactly = 0) { listener.handleCreated(any(), any()) }
        verify(exactly = 0) { listener.handleUpdated(any(), any()) }
    }

    @Test
    fun getForAccount() {
        val actor = AccountTestData.createActor(accountStorage)
        val account = bareAccount()
        val profile = ProfileTestData.create(service, actor, account)

        assertEquals(profile, service.getForAccount(account))
        assertNull(service.getForAccount(bareAccount("without-profile")))
    }

    @Test
    fun getForAccounts() {
        val actor = AccountTestData.createActor(accountStorage)
        val a1 = bareAccount("a1")
        val a2 = bareAccount("a2")
        val first = ProfileTestData.create(service, actor, a1, ProfileTestData.createRequest(email = "p1@example.com"))
        val second = ProfileTestData.create(service, actor, a2, ProfileTestData.createRequest(email = "p2@example.com"))

        assertEquals(setOf(first, second), service.getForAccounts(listOf(a1, a2)).toSet())
    }

    @Test
    fun mergeCreatesWhenMissing() {
        val actor = AccountTestData.createActor(accountStorage)
        val account = bareAccount()
        val listener = registerListener()

        val request = ProfileTestData.createRequest()
        val profile = service.merge(actor, account, request)

        assertEquals(account.id, profile.id)
        assertEquals(request.email, profile.email)
        assertEquals(request.firstName, profile.firstName)
        assertEquals(profile, service.getForAccount(account))
        // merge uses the account itself as the notification actor
        verify { listener.handleCreated(account, profile) }
    }

    @Test
    fun mergeUpdatesWhenExisting() {
        val actor = AccountTestData.createActor(accountStorage)
        val account = bareAccount()
        val existing = ProfileTestData.create(
            service, actor, account,
            ProfileTestData.createRequest(email = "orig@example.com", firstName = "orig-first", lastName = "orig-last"),
        )
        val listener = registerListener()

        val request = ProfileTestData.createRequest(email = "new@example.com", firstName = "merged-first", lastName = "merged-last")
        val merged = service.merge(actor, account, request)

        assertEquals(existing.id, merged.id)
        // existing non-blank fields (email) are retained, the name is overwritten by the request
        assertEquals("orig@example.com", merged.email)
        assertEquals("merged-first", merged.firstName)
        assertEquals("merged-last", merged.lastName)
        verify { listener.handleUpdated(account, merged) }
    }

    @Test
    fun notifications() {
        val actor = AccountTestData.createActor(accountStorage)
        val account = bareAccount()
        val listener = registerListener()

        val profile = service.create(actor, account, ProfileTestData.createRequest())
        verify(exactly = 1) { listener.handleCreated(actor, profile) }
        verify(exactly = 0) { listener.handleUpdated(any(), any()) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        val updated = service.update(actor, profile.id, ProfileTestData.updateRequest())
        verify(exactly = 1) { listener.handleUpdated(actor, updated) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        service.delete(actor, profile.id)
        verify(exactly = 1) { listener.handleDeleted(actor, updated) }
    }

}

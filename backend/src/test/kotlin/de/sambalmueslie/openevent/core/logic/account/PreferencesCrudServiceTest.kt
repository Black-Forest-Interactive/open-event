package de.sambalmueslie.openevent.core.logic.account

import de.sambalmueslie.openevent.TimeBasedTest
import de.sambalmueslie.openevent.core.account.AccountStorage
import de.sambalmueslie.openevent.core.account.PreferencesChangeListener
import de.sambalmueslie.openevent.core.account.PreferencesCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.testdata.AccountTestData
import de.sambalmueslie.openevent.testdata.PreferencesTestData
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.*
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest
class PreferencesCrudServiceTest : TimeBasedTest() {

    @Inject
    lateinit var accountStorage: AccountStorage

    @Inject
    lateinit var service: PreferencesCrudService

    private fun bareAccount(externalId: String = "account-id"): Account =
        accountStorage.create(AccountTestData.accountRequest(externalId = externalId))

    private fun registerListener(): PreferencesChangeListener {
        val listener = mockk<PreferencesChangeListener>()
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

        val preferences = service.create(actor, account, PreferencesTestData.createRequest())

        assertEquals(account.id, preferences.id)
        assertFalse(preferences.emailNotificationsPreferences.enabled)
        assertFalse(preferences.communicationPreferences.enabled)
        assertFalse(preferences.notificationPreferences.enabled)
        verify { listener.handleCreated(actor, preferences) }
    }

    @Test
    fun get() {
        val actor = AccountTestData.createActor(accountStorage)
        val preferences = PreferencesTestData.create(service, actor, bareAccount())

        assertEquals(preferences, service.get(preferences.id))
        assertNull(service.get(-1))
    }

    @Test
    fun getAll() {
        val actor = AccountTestData.createActor(accountStorage)
        val first = PreferencesTestData.create(service, actor, bareAccount("a1"))
        val second = PreferencesTestData.create(service, actor, bareAccount("a2"))

        assertEquals(setOf(first, second), service.getAll(Pageable.from(0)).content.toSet())
    }

    @Test
    fun update() {
        val actor = AccountTestData.createActor(accountStorage)
        val preferences = PreferencesTestData.create(service, actor, bareAccount())
        val listener = registerListener()

        val updated = service.update(actor, preferences.id, PreferencesTestData.updateRequest())

        assertEquals(preferences.id, updated.id)
        assertTrue(updated.emailNotificationsPreferences.enabled)
        assertTrue(updated.communicationPreferences.enabled)
        assertTrue(updated.notificationPreferences.enabled)
        verify { listener.handleUpdated(actor, updated) }
    }

    @Test
    fun delete() {
        val actor = AccountTestData.createActor(accountStorage)
        val preferences = PreferencesTestData.create(service, actor, bareAccount())
        val listener = registerListener()

        val deleted = service.delete(actor, preferences.id)

        assertEquals(preferences, deleted)
        assertNull(service.get(preferences.id))
        verify(exactly = 1) { listener.handleDeleted(actor, preferences) }
        verify(exactly = 0) { listener.handleCreated(any(), any()) }
        verify(exactly = 0) { listener.handleUpdated(any(), any()) }
    }

    @Test
    fun getForAccount() {
        val actor = AccountTestData.createActor(accountStorage)
        val account = bareAccount()
        val preferences = PreferencesTestData.create(service, actor, account)

        assertEquals(preferences, service.getForAccount(account))
        assertNull(service.getForAccount(bareAccount("without-preferences")))
    }

    @Test
    fun notifications() {
        val actor = AccountTestData.createActor(accountStorage)
        val account = bareAccount()
        val listener = registerListener()

        val preferences = service.create(actor, account, PreferencesTestData.createRequest())
        verify(exactly = 1) { listener.handleCreated(actor, preferences) }
        verify(exactly = 0) { listener.handleUpdated(any(), any()) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        val updated = service.update(actor, preferences.id, PreferencesTestData.updateRequest())
        verify(exactly = 1) { listener.handleUpdated(actor, updated) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        service.delete(actor, preferences.id)
        verify(exactly = 1) { listener.handleDeleted(actor, updated) }
    }

}

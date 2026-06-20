package de.sambalmueslie.openevent.core.logic.address

import de.sambalmueslie.openevent.TimeBasedTest
import de.sambalmueslie.openevent.core.account.AccountStorage
import de.sambalmueslie.openevent.core.address.AddressChangeListener
import de.sambalmueslie.openevent.core.address.AddressCrudService
import de.sambalmueslie.openevent.core.address.api.AddressChangeRequest
import de.sambalmueslie.openevent.infrastructure.geo.GeoLocationResolver
import de.sambalmueslie.openevent.testdata.AccountTestData
import de.sambalmueslie.openevent.testdata.AddressTestData
import io.micronaut.data.model.Pageable
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.*
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest
class AddressCrudServiceTest : TimeBasedTest() {

    @Inject
    lateinit var accountStorage: AccountStorage

    @Inject
    lateinit var service: AddressCrudService

    private val geoResolver = mockk<GeoLocationResolver>()

    @MockBean(GeoLocationResolver::class)
    fun geoResolver() = geoResolver

    init {
        every { geoResolver.get(any<AddressChangeRequest>()) } returns null
    }

    private fun registerListener(): AddressChangeListener {
        val listener = mockk<AddressChangeListener>()
        service.register(listener)
        every { listener.handleCreated(any(), any()) } just Runs
        every { listener.handleUpdated(any(), any()) } just Runs
        every { listener.handleDeleted(any(), any()) } just Runs
        return listener
    }

    @Test
    fun create() {
        val account = AccountTestData.createActor(accountStorage)
        val listener = registerListener()

        val request = AddressTestData.createRequest()
        val address = service.create(account, account, request)

        assertTrue(address.id > 0)
        assertEquals(request.street, address.street)
        assertEquals(request.city, address.city)
        assertEquals(request.lat, address.lat)
        assertEquals(request.lon, address.lon)
        verify { listener.handleCreated(account, address) }
    }

    @Test
    fun get() {
        val account = AccountTestData.createActor(accountStorage)
        val address = AddressTestData.create(service, account, account)

        assertEquals(address, service.get(address.id))
        assertNull(service.get(-1))
    }

    @Test
    fun getAll() {
        val account = AccountTestData.createActor(accountStorage)
        val first = AddressTestData.create(service, account, account, AddressTestData.createRequest(street = "first"))
        val second = AddressTestData.create(service, account, account, AddressTestData.createRequest(street = "second"))

        assertEquals(setOf(first, second), service.getAll(Pageable.from(0)).content.toSet())
    }

    @Test
    fun update() {
        val account = AccountTestData.createActor(accountStorage)
        val address = AddressTestData.create(service, account, account)
        val listener = registerListener()

        val request = AddressTestData.updateRequest()
        val updated = service.update(account, address.id, request)

        assertEquals(address.id, updated.id)
        assertEquals(request.street, updated.street)
        assertEquals(request.city, updated.city)
        verify { listener.handleUpdated(account, updated) }
    }

    @Test
    fun delete() {
        val account = AccountTestData.createActor(accountStorage)
        val address = AddressTestData.create(service, account, account)
        val listener = registerListener()

        val deleted = service.delete(account, address.id)

        assertEquals(address, deleted)
        assertNull(service.get(address.id))
        verify(exactly = 1) { listener.handleDeleted(account, address) }
        verify(exactly = 0) { listener.handleCreated(any(), any()) }
        verify(exactly = 0) { listener.handleUpdated(any(), any()) }
    }

    @Test
    fun getAllForAccount() {
        val account = AccountTestData.createActor(accountStorage)
        val other = accountStorage.create(AccountTestData.accountRequest(externalId = "other"))
        val address = AddressTestData.create(service, account, account)

        assertEquals(listOf(address), service.getAllForAccount(account, Pageable.from(0)).content)
        assertTrue(service.getAllForAccount(other, Pageable.from(0)).content.isEmpty())
    }

    @Test
    fun getForAccount() {
        val account = AccountTestData.createActor(accountStorage)
        val other = accountStorage.create(AccountTestData.accountRequest(externalId = "other"))
        val address = AddressTestData.create(service, account, account)

        assertEquals(address, service.getForAccount(account, address.id))
        assertNull(service.getForAccount(other, address.id))
    }

    @Test
    fun getData() {
        val account = AccountTestData.createActor(accountStorage)
        val address = AddressTestData.create(service, account, account)

        val data = service.getData(address.id)
        assertNotNull(data)
        assertEquals(address.id, data!!.id)
        assertNull(service.getData(-1))
    }

    @Test
    fun setDefault() {
        val account = AccountTestData.createActor(accountStorage)
        val address = AddressTestData.create(service, account, account)
        val listener = registerListener()

        val result = service.setDefault(account, address.id)

        assertNotNull(result)
        assertEquals(address.id, result!!.id)
        assertTrue(result.standard)
        verify(exactly = 1) { listener.handleUpdated(account, any()) }
    }

    @Test
    fun notifications() {
        val account = AccountTestData.createActor(accountStorage)
        val listener = registerListener()

        val address = service.create(account, account, AddressTestData.createRequest())
        verify(exactly = 1) { listener.handleCreated(account, address) }
        verify(exactly = 0) { listener.handleUpdated(any(), any()) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        val updated = service.update(account, address.id, AddressTestData.updateRequest())
        verify(exactly = 1) { listener.handleUpdated(account, updated) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        service.delete(account, address.id)
        verify(exactly = 1) { listener.handleDeleted(account, updated) }
    }

}

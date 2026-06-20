package de.sambalmueslie.openevent.core.logic.location

import de.sambalmueslie.openevent.TimeBasedTest
import de.sambalmueslie.openevent.core.account.AccountStorage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.location.LocationChangeListener
import de.sambalmueslie.openevent.core.location.LocationCrudService
import de.sambalmueslie.openevent.core.location.api.LocationChangeRequest
import de.sambalmueslie.openevent.infrastructure.geo.GeoLocationResolver
import de.sambalmueslie.openevent.testdata.AccountTestData
import de.sambalmueslie.openevent.testdata.EventTestData
import de.sambalmueslie.openevent.testdata.LocationTestData
import io.micronaut.data.model.Pageable
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.*
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest
class LocationCrudServiceTest : TimeBasedTest() {

    @Inject
    lateinit var accountStorage: AccountStorage

    @Inject
    lateinit var eventService: EventCrudService

    @Inject
    lateinit var service: LocationCrudService

    private val geoResolver = mockk<GeoLocationResolver>()

    @MockBean(GeoLocationResolver::class)
    fun geoResolver() = geoResolver

    init {
        every { geoResolver.get(any<LocationChangeRequest>()) } returns null
    }

    private fun createEvent(actor: Account, location: LocationChangeRequest? = null): Event =
        EventTestData.create(eventService, actor, EventTestData.createRequest(location = location))

    private fun registerListener(): LocationChangeListener {
        val listener = mockk<LocationChangeListener>()
        service.register(listener)
        every { listener.handleCreated(any(), any()) } just Runs
        every { listener.handleUpdated(any(), any()) } just Runs
        every { listener.handleDeleted(any(), any()) } just Runs
        return listener
    }

    @Test
    fun create() {
        val actor = AccountTestData.createActor(accountStorage)
        val event = createEvent(actor)
        val listener = registerListener()

        val request = LocationTestData.createRequest()
        val location = service.create(actor, event, request)

        assertTrue(location.id > 0)
        assertEquals(event.id, location.eventId)
        assertEquals(request.street, location.street)
        assertEquals(request.city, location.city)
        assertEquals(request.lat, location.lat)
        assertEquals(request.lon, location.lon)
        assertEquals(request.size, location.size)
        verify { listener.handleCreated(actor, location) }
    }

    @Test
    fun get() {
        val actor = AccountTestData.createActor(accountStorage)
        val event = createEvent(actor, LocationTestData.createRequest())
        val location = service.findByEvent(event)!!

        assertEquals(location, service.get(location.id))
        assertNull(service.get(-1))
    }

    @Test
    fun getAll() {
        val actor = AccountTestData.createActor(accountStorage)
        val first = service.findByEvent(createEvent(actor, LocationTestData.createRequest()))!!
        val second = service.findByEvent(createEvent(actor, LocationTestData.createRequest()))!!

        assertEquals(setOf(first, second), service.getAll(Pageable.from(0)).content.toSet())
    }

    @Test
    fun update() {
        val actor = AccountTestData.createActor(accountStorage)
        val location = service.findByEvent(createEvent(actor, LocationTestData.createRequest()))!!
        val listener = registerListener()

        val request = LocationTestData.updateRequest()
        val updated = service.update(actor, location.id, request)

        assertEquals(location.id, updated.id)
        assertEquals(request.street, updated.street)
        assertEquals(request.lat, updated.lat)
        assertEquals(request.size, updated.size)
        verify { listener.handleUpdated(actor, updated) }
    }

    @Test
    fun delete() {
        val actor = AccountTestData.createActor(accountStorage)
        val location = service.findByEvent(createEvent(actor, LocationTestData.createRequest()))!!
        val listener = registerListener()

        val deleted = service.delete(actor, location.id)

        assertEquals(location, deleted)
        assertNull(service.get(location.id))
        verify(exactly = 1) { listener.handleDeleted(actor, location) }
        verify(exactly = 0) { listener.handleCreated(any(), any()) }
        verify(exactly = 0) { listener.handleUpdated(any(), any()) }
    }

    @Test
    fun findByEvent() {
        val actor = AccountTestData.createActor(accountStorage)
        val event = createEvent(actor, LocationTestData.createRequest())

        assertNotNull(service.findByEvent(event))
        assertNull(service.findByEvent(createEvent(actor)))
    }

    @Test
    fun updateByEventCreatesWhenMissing() {
        val actor = AccountTestData.createActor(accountStorage)
        val event = createEvent(actor)
        val listener = registerListener()

        val request = LocationTestData.createRequest()
        val result = service.updateByEvent(actor, event, request)

        assertEquals(event.id, result.eventId)
        assertEquals(request.street, result.street)
        assertEquals(result, service.findByEvent(event))
        verify { listener.handleCreated(actor, result) }
    }

    @Test
    fun updateByEventUpdatesWhenExisting() {
        val actor = AccountTestData.createActor(accountStorage)
        val event = createEvent(actor, LocationTestData.createRequest())
        val listener = registerListener()

        val request = LocationTestData.updateRequest()
        val result = service.updateByEvent(actor, event, request)

        assertEquals(request.street, result.street)
        verify { listener.handleUpdated(actor, result) }
    }

    @Test
    fun deleteByEvent() {
        val actor = AccountTestData.createActor(accountStorage)
        val event = createEvent(actor, LocationTestData.createRequest())
        val location = service.findByEvent(event)!!
        val listener = registerListener()

        val deleted = service.deleteByEvent(actor, event)

        assertEquals(location, deleted)
        assertNull(service.findByEvent(event))
        verify { listener.handleDeleted(actor, location) }
    }

    @Test
    fun findByEventIds() {
        val actor = AccountTestData.createActor(accountStorage)
        val e1 = createEvent(actor, LocationTestData.createRequest())
        val e2 = createEvent(actor, LocationTestData.createRequest())
        val l1 = service.findByEvent(e1)!!
        val l2 = service.findByEvent(e2)!!

        assertEquals(setOf(l1, l2), service.findByEventIds(setOf(e1.id, e2.id)).toSet())
    }

    @Test
    fun notifications() {
        val actor = AccountTestData.createActor(accountStorage)
        val event = createEvent(actor)
        val listener = registerListener()

        val location = service.create(actor, event, LocationTestData.createRequest())
        verify(exactly = 1) { listener.handleCreated(actor, location) }
        verify(exactly = 0) { listener.handleUpdated(any(), any()) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        val updated = service.update(actor, location.id, LocationTestData.updateRequest())
        verify(exactly = 1) { listener.handleUpdated(actor, updated) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        service.delete(actor, location.id)
        verify(exactly = 1) { listener.handleDeleted(actor, updated) }
    }

}

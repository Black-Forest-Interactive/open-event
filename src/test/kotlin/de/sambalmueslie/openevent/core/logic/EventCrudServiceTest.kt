package de.sambalmueslie.openevent.core.logic

import de.sambalmueslie.openevent.TimeBasedTest
import de.sambalmueslie.openevent.core.logic.account.AccountCrudService
import de.sambalmueslie.openevent.core.logic.event.EventChangeListener
import de.sambalmueslie.openevent.core.logic.event.EventCrudService
import de.sambalmueslie.openevent.core.logic.location.LocationChangeListener
import de.sambalmueslie.openevent.core.logic.location.LocationCrudService
import de.sambalmueslie.openevent.core.logic.registration.RegistrationChangeListener
import de.sambalmueslie.openevent.core.logic.registration.RegistrationCrudService
import de.sambalmueslie.openevent.core.model.*
import de.sambalmueslie.openevent.core.storage.AccountStorage
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.*
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@MicronautTest
class EventCrudServiceTest : TimeBasedTest() {

    @Inject
    lateinit var accountStorage: AccountStorage
    @Inject
    lateinit var accountService: AccountCrudService

    @Inject
    lateinit var service: EventCrudService

    @Inject
    lateinit var locationService: LocationCrudService

    @Inject
    lateinit var registrationService: RegistrationCrudService

    private val eventListener = mockk<EventChangeListener>()
    private val locationListener = mockk<LocationChangeListener>()
    private val registrationListener = mockk<RegistrationChangeListener>()

    @Test
    fun eventCrud() {
        val actor = accountStorage.create(AccountChangeRequest("user", "first", "last", "email@localhost", "", ""))
        setupListener()

        val owner =
            accountService.create(actor, AccountChangeRequest("name", "first-name", "last-name", "email", "", null))

        val request = buildCreateRequest()
        var event = service.create(actor, request)
        verify { eventListener.handleCreated(actor, event) }
        verify { locationListener.handleCreated(actor, locationService.findByEvent(event)!!) }
        verify { registrationListener.handleCreated(actor, registrationService.findByEvent(event)!!) }
        validate(request, event)

        assertEquals(event, service.get(event.id))
        assertEquals(listOf(event), service.getAll(Pageable.from(0)).content)

        val update = buildUpdateRequest(request)
        event = service.update(actor, event.id, update)
        verify { eventListener.handleUpdated(actor, event) }
        verify { locationListener.handleUpdated(actor, locationService.findByEvent(event)!!) }
        verify { registrationListener.handleUpdated(actor, registrationService.findByEvent(event)!!) }
        validate(update, event)

        val location = locationService.findByEvent(event)!!
        val registration = registrationService.findByEvent(event)!!
        service.delete(actor, event.id)
        verify { eventListener.handleDeleted(actor, event) }
        verify { locationListener.handleDeleted(actor, location) }
        verify { registrationListener.handleDeleted(actor, registration) }
        assertEquals(emptyList<Event>(), service.getAll(Pageable.from(0)).content)
        assertEquals(emptyList<Location>(), locationService.getAll(Pageable.from(0)).content)
        assertEquals(emptyList<Registration>(), registrationService.getAll(Pageable.from(0)).content)

    }

    private fun buildUpdateRequest(request: EventChangeRequest): EventChangeRequest {
        val locationUpdate = LocationChangeRequest(
            "street-update",
            "nr-update",
            "zip-update",
            "city-update",
            "country-update",
            "info-update",
            10.0,
            20.0,
            30
        )
        val registrationUpdate = RegistrationChangeRequest(100, false, true)
        val update = EventChangeRequest(
            request.start.plusDays(1), request.finish.plusDays(1),
            "title-update",
            "short-update",
            "long-update",
            "image-update",
            "icon-update",
            emptySet(),
            locationUpdate, registrationUpdate
        )
        return update
    }

    private fun buildCreateRequest(): EventChangeRequest {
        val locationRequest = LocationChangeRequest("street", "nr", "zip", "city", "country", "info", 1.0, 2.0, 3)
        val registrationRequest = RegistrationChangeRequest(10, true, false)
        val request = EventChangeRequest(
            LocalDateTime.of(2023, 10, 1, 20, 15),
            LocalDateTime.of(2023, 10, 1, 22, 15),
            "title",
            "short",
            "long",
            "image",
            "icon",
            emptySet(),
            locationRequest, registrationRequest
        )
        return request
    }

    private fun setupListener() {
        service.register(eventListener)
        every { eventListener.handleCreated(any(), any()) } just Runs
        every { eventListener.handleUpdated(any(), any()) } just Runs
        every { eventListener.handleDeleted(any(), any()) } just Runs

        locationService.register(locationListener)
        every { locationListener.handleCreated(any(), any()) } just Runs
        every { locationListener.handleUpdated(any(), any()) } just Runs
        every { locationListener.handleDeleted(any(), any()) } just Runs

        registrationService.register(registrationListener)
        every { registrationListener.handleCreated(any(), any()) } just Runs
        every { registrationListener.handleUpdated(any(), any()) } just Runs
        every { registrationListener.handleDeleted(any(), any()) } just Runs
    }

    private fun validate(request: EventChangeRequest, event: Event) {
        assertEquals(request.start, event.start)
        assertEquals(request.finish, event.finish)
        assertEquals(request.title, event.title)
        assertEquals(request.shortText, event.shortText)
        assertEquals(request.longText, event.longText)
        assertEquals(request.imageUrl, event.imageUrl)
        assertEquals(request.iconUrl, event.iconUrl)

        val locationRequest = request.location
        if (locationRequest != null) {
            val location = locationService.findByEvent(event)!!
            assertEquals(locationRequest.street, location.street)
            assertEquals(locationRequest.streetNumber, location.streetNumber)
            assertEquals(locationRequest.zip, location.zip)
            assertEquals(locationRequest.city, location.city)
            assertEquals(locationRequest.country, location.country)
            assertEquals(locationRequest.additionalInfo, location.additionalInfo)
            assertEquals(locationRequest.lat, location.lat)
            assertEquals(locationRequest.lon, location.lon)
            assertEquals(locationRequest.size, location.size)
        }

        val registrationRequest = request.registration
        val registration = registrationService.findByEvent(event)
        assertNotNull(registration)
        assertEquals(registrationRequest.maxGuestAmount, registration!!.maxGuestAmount)
        assertEquals(registrationRequest.interestedAllowed, registration.interestedAllowed)
        assertEquals(registrationRequest.interestedAllowed, registration.interestedAllowed)
        assertEquals(registrationRequest.ticketsEnabled, registration.ticketsEnabled)
    }

}

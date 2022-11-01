package de.sambalmueslie.openevent.location

import de.sambalmueslie.openevent.common.time.TimeProvider
import de.sambalmueslie.openevent.location.api.*
import io.micronaut.data.model.Pageable
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@MicronautTest
internal class LocationServiceTest {

    @Inject
    lateinit var service: LocationService

    private val timeProvider = mockk<TimeProvider>()

    @MockBean(TimeProvider::class)
    fun timeProvider() = timeProvider

    private val listener = mockk<LocationChangeListener>()

    @Test
    fun checkCrud() {
        val now = LocalDateTime.of(2022, 10, 8, 20, 15, 0)
        every { timeProvider.now() } returns now
        service.register(listener)

        // create
        val createRequest = LocationChangeRequest(
            AddressChangeRequest("street", "streetNumber", "zip", "city", "country", "additional-info"),
            GeoLocationChangeRequest(1.0, 2.0),
            3
        )
        var result = service.create(createRequest)

        var reference = Location(
            1,
            Address(
                1,
                createRequest.address.street,
                createRequest.address.streetNumber,
                createRequest.address.zip,
                createRequest.address.city,
                createRequest.address.country,
                createRequest.address.additionalInfo
            ),
            GeoLocation(1, createRequest.geoLocation.lat, createRequest.geoLocation.lon),
            createRequest.capacity
        )
        assertEquals(reference, result)
        verify { listener.handleCreated(reference) }
        verify { listener.hashCode() }
        confirmVerified(listener)

        // read
        assertEquals(reference, service.get(reference.id))
        assertEquals(listOf(reference), service.getAll(Pageable.from(0)).content)

        // update
        val updateRequest = LocationChangeRequest(
            AddressChangeRequest("update-street", "update-streetNumber", "update-zip", "update-city", "update-country", "update-additional-info"),
            GeoLocationChangeRequest(3.0, 4.0),
            4
        )
        result = service.update(reference.id, updateRequest)

        reference = Location(
            1,
            Address(
                1,
                updateRequest.address.street,
                updateRequest.address.streetNumber,
                updateRequest.address.zip,
                updateRequest.address.city,
                updateRequest.address.country,
                updateRequest.address.additionalInfo
            ),
            GeoLocation(1, updateRequest.geoLocation.lat, updateRequest.geoLocation.lon),
            updateRequest.capacity
        )
        assertEquals(reference, result)
        verify { listener.handleUpdated(reference) }
        verify { listener.hashCode() }
        confirmVerified(listener)

        // delete
        service.delete(reference.id)

        // read empty
        assertEquals(null, service.get(reference.id))
        assertEquals(emptyList<Location>(), service.getAll(Pageable.from(0)).content)
    }
}

package de.sambalmueslie.openevent.core.logic

import de.sambalmueslie.openevent.TimeBasedTest
import de.sambalmueslie.openevent.core.account.AccountStorage
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.contact.EventContactChangeRequest
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.testdata.AccountTestData
import de.sambalmueslie.openevent.testdata.EventTestData
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

@MicronautTest
class EventContactTest : TimeBasedTest() {

    @Inject
    lateinit var accountStorage: AccountStorage

    @Inject
    lateinit var service: EventCrudService

    @Test
    fun contactLifecycle() {
        val actor = AccountTestData.createActor(accountStorage)
        val event = service.create(actor, EventTestData.createRequest().copy(contact = EventContactChangeRequest(" 0761 1234 ", "")))
        assertEquals("0761 1234", service.getContact(event.id)?.phone)

        // missing contact in request keeps the stored one
        service.update(actor, event.id, EventTestData.createRequest())
        assertEquals("0761 1234", service.getContact(event.id)?.phone)

        service.update(actor, event.id, EventTestData.createRequest().copy(contact = EventContactChangeRequest("", "+49 170 123")))
        assertEquals("", service.getContact(event.id)?.phone)
        assertEquals("+49 170 123", service.getContact(event.id)?.mobile)

        // empty contact removes it
        service.update(actor, event.id, EventTestData.createRequest().copy(contact = EventContactChangeRequest("", "")))
        assertNull(service.getContact(event.id))

        service.update(actor, event.id, EventTestData.createRequest().copy(contact = EventContactChangeRequest("0761", "")))
        service.delete(actor, event.id)
        assertNull(service.getContact(event.id))
    }

    @Test
    fun rejectsInvalidPhone() {
        val actor = AccountTestData.createActor(accountStorage)
        assertThrows(InvalidRequestException::class.java) {
            service.create(actor, EventTestData.createRequest().copy(contact = EventContactChangeRequest("<script>", "")))
        }
    }
}

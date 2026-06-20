package de.sambalmueslie.openevent.core.logic.activity

import de.sambalmueslie.openevent.TimeBasedTest
import de.sambalmueslie.openevent.core.account.AccountStorage
import de.sambalmueslie.openevent.core.activity.ActivityChangeListener
import de.sambalmueslie.openevent.core.activity.ActivityCrudService
import de.sambalmueslie.openevent.core.activity.ActivitySourceStorage
import de.sambalmueslie.openevent.core.activity.ActivityTypeStorage
import de.sambalmueslie.openevent.core.activity.api.ActivitySource
import de.sambalmueslie.openevent.core.activity.api.ActivitySourceChangeRequest
import de.sambalmueslie.openevent.core.activity.api.ActivityType
import de.sambalmueslie.openevent.core.activity.api.ActivityTypeChangeRequest
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.testdata.AccountTestData
import de.sambalmueslie.openevent.testdata.ActivityTestData
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.*
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest
class ActivityCrudServiceTest : TimeBasedTest() {

    @Inject
    lateinit var accountStorage: AccountStorage

    @Inject
    lateinit var service: ActivityCrudService

    @Inject
    lateinit var sourceStorage: ActivitySourceStorage

    @Inject
    lateinit var typeStorage: ActivityTypeStorage

    private fun source(): ActivitySource = sourceStorage.create(ActivitySourceChangeRequest("source-key"))
    private fun type(source: ActivitySource): ActivityType = typeStorage.create(ActivityTypeChangeRequest("type-key"), source)

    private fun registerListener(): ActivityChangeListener {
        val listener = mockk<ActivityChangeListener>()
        service.register(listener)
        every { listener.handleCreated(any(), any()) } just Runs
        every { listener.handleUpdated(any(), any()) } just Runs
        every { listener.handleDeleted(any(), any()) } just Runs
        return listener
    }

    @Test
    fun create() {
        val actor = AccountTestData.createActor(accountStorage)
        val source = source()
        val type = type(source)
        val listener = registerListener()

        val request = ActivityTestData.createRequest()
        val activity = service.create(actor, request, source, type)

        assertTrue(activity.id > 0)
        assertEquals(request.title, activity.title)
        assertEquals(request.referenceId, activity.referenceId)
        assertEquals(source.key, activity.source)
        assertEquals(type.key, activity.type)
        assertEquals(actor.id, activity.actor.id)
        verify { listener.handleCreated(actor, activity) }
    }

    @Test
    fun get() {
        val actor = AccountTestData.createActor(accountStorage)
        val source = source()
        val activity = ActivityTestData.create(service, actor, source, type(source))

        assertEquals(activity, service.get(activity.id))
        assertNull(service.get(-1))
    }

    @Test
    fun getForAccount() {
        val actor = AccountTestData.createActor(accountStorage)
        val other = accountStorage.create(AccountTestData.accountRequest(externalId = "other"))
        val source = source()
        val activity = ActivityTestData.create(service, actor, source, type(source))

        assertEquals(activity, service.getForAccount(actor, activity.id))
        assertNull(service.getForAccount(other, activity.id))
    }

    @Test
    fun getAll() {
        val actor = AccountTestData.createActor(accountStorage)
        val source = source()
        val type = type(source)
        val first = ActivityTestData.create(service, actor, source, type, ActivityTestData.createRequest("first"))
        val second = ActivityTestData.create(service, actor, source, type, ActivityTestData.createRequest("second"))

        assertEquals(setOf(first, second), service.getAll(Pageable.from(0)).content.toSet())
    }

    @Test
    fun update() {
        val actor = AccountTestData.createActor(accountStorage)
        val source = source()
        val activity = ActivityTestData.create(service, actor, source, type(source))
        val listener = registerListener()

        val request = ActivityTestData.updateRequest()
        val updated = service.update(actor, activity.id, request)

        assertEquals(activity.id, updated.id)
        assertEquals(request.title, updated.title)
        assertEquals(request.referenceId, updated.referenceId)
        assertEquals(source.key, updated.source)
        verify { listener.handleUpdated(actor, updated) }
    }

    @Test
    fun delete() {
        val actor = AccountTestData.createActor(accountStorage)
        val source = source()
        val activity = ActivityTestData.create(service, actor, source, type(source))
        val listener = registerListener()

        val deleted = service.delete(actor, activity.id)

        assertEquals(activity, deleted)
        assertNull(service.get(activity.id))
        verify(exactly = 1) { listener.handleDeleted(actor, activity) }
        verify(exactly = 0) { listener.handleCreated(any(), any()) }
        verify(exactly = 0) { listener.handleUpdated(any(), any()) }
    }

    @Test
    fun isValidRejectsBlankTitleOnCreate() {
        val actor = AccountTestData.createActor(accountStorage)
        val source = source()
        val type = type(source)

        assertThrows(InvalidRequestException::class.java) {
            service.create(actor, ActivityTestData.createRequest(title = " "), source, type)
        }
    }

    @Test
    fun isValidRejectsInvalidReferenceIdOnCreate() {
        val actor = AccountTestData.createActor(accountStorage)
        val source = source()
        val type = type(source)

        assertThrows(InvalidRequestException::class.java) {
            service.create(actor, ActivityTestData.createRequest(referenceId = 0), source, type)
        }
    }

    @Test
    fun isValidRejectsBlankTitleOnUpdate() {
        val actor = AccountTestData.createActor(accountStorage)
        val source = source()
        val activity = ActivityTestData.create(service, actor, source, type(source))

        assertThrows(InvalidRequestException::class.java) {
            service.update(actor, activity.id, ActivityTestData.updateRequest(title = " "))
        }
    }

    @Test
    fun notifications() {
        val actor = AccountTestData.createActor(accountStorage)
        val source = source()
        val type = type(source)
        val listener = registerListener()

        val activity = service.create(actor, ActivityTestData.createRequest(), source, type)
        verify(exactly = 1) { listener.handleCreated(actor, activity) }
        verify(exactly = 0) { listener.handleUpdated(any(), any()) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        val updated = service.update(actor, activity.id, ActivityTestData.updateRequest())
        verify(exactly = 1) { listener.handleUpdated(actor, updated) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        service.delete(actor, activity.id)
        verify(exactly = 1) { listener.handleDeleted(actor, updated) }
    }

}

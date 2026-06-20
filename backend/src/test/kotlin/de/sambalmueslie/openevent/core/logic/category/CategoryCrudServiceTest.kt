package de.sambalmueslie.openevent.core.logic.category

import de.sambalmueslie.openevent.TimeBasedTest
import de.sambalmueslie.openevent.core.account.AccountStorage
import de.sambalmueslie.openevent.core.category.CategoryChangeListener
import de.sambalmueslie.openevent.core.category.CategoryCrudService
import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.testdata.AccountTestData
import de.sambalmueslie.openevent.testdata.CategoryTestData
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.*
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest
class CategoryCrudServiceTest : TimeBasedTest() {

    @Inject
    lateinit var accountStorage: AccountStorage

    @Inject
    lateinit var service: CategoryCrudService

    private fun registerListener(): CategoryChangeListener {
        val listener = mockk<CategoryChangeListener>()
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

        val request = CategoryTestData.createRequest()
        val category = service.create(actor, request)

        assertTrue(category.id > 0)
        assertEquals(request.name, category.name)
        assertEquals(request.iconUrl, category.iconUrl)
        verify { listener.handleCreated(actor, category) }
    }

    @Test
    fun get() {
        val actor = AccountTestData.createActor(accountStorage)
        val category = CategoryTestData.create(service, actor)

        assertEquals(category, service.get(category.id))
        assertEquals(listOf(category), service.getByIds(setOf(category.id)))
        assertNull(service.get(-1))
    }

    @Test
    fun getAll() {
        val actor = AccountTestData.createActor(accountStorage)
        val first = CategoryTestData.create(service, actor, CategoryTestData.createRequest("first", "first-icon"))
        val second = CategoryTestData.create(service, actor, CategoryTestData.createRequest("second", "second-icon"))

        val content = service.getAll(Pageable.from(0)).content
        assertEquals(setOf(first, second), content.toSet())
    }

    @Test
    fun update() {
        val actor = AccountTestData.createActor(accountStorage)
        val listener = registerListener()
        val category = CategoryTestData.create(service, actor)

        val request = CategoryTestData.updateRequest()
        val updated = service.update(actor, category.id, request)

        assertEquals(category.id, updated.id)
        assertEquals(request.name, updated.name)
        assertEquals(request.iconUrl, updated.iconUrl)
        verify { listener.handleUpdated(actor, updated) }
    }

    @Test
    fun delete() {
        val actor = AccountTestData.createActor(accountStorage)
        val listener = registerListener()
        val category = CategoryTestData.create(service, actor)

        val deleted = service.delete(actor, category.id)

        assertEquals(category, deleted)
        assertNull(service.get(category.id))
        assertEquals(emptyList<Category>(), service.getAll(Pageable.from(0)).content)
        verify { listener.handleDeleted(actor, category) }
    }

    @Test
    fun findByName() {
        val actor = AccountTestData.createActor(accountStorage)
        val request = CategoryTestData.createRequest()
        val category = CategoryTestData.create(service, actor, request)

        assertEquals(category, service.findByName(request.name))
        assertNull(service.findByName("unknown"))
    }

    @Test
    fun isValidRejectsBlankNameOnCreate() {
        val actor = AccountTestData.createActor(accountStorage)

        assertThrows(InvalidRequestException::class.java) {
            service.create(actor, CategoryTestData.createRequest(name = " "))
        }
    }

    @Test
    fun isValidRejectsBlankNameOnUpdate() {
        val actor = AccountTestData.createActor(accountStorage)
        val category = CategoryTestData.create(service, actor)

        assertThrows(InvalidRequestException::class.java) {
            service.update(actor, category.id, CategoryTestData.updateRequest(name = " "))
        }
    }

    @Test
    fun notifications() {
        val actor = AccountTestData.createActor(accountStorage)
        val listener = registerListener()

        val category = service.create(actor, CategoryTestData.createRequest())
        verify(exactly = 1) { listener.handleCreated(actor, category) }
        verify(exactly = 0) { listener.handleUpdated(any(), any()) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        val updated = service.update(actor, category.id, CategoryTestData.updateRequest())
        verify(exactly = 1) { listener.handleUpdated(actor, updated) }
        verify(exactly = 1) { listener.handleCreated(any(), any()) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        service.delete(actor, category.id)
        verify(exactly = 1) { listener.handleDeleted(actor, updated) }
        verify(exactly = 1) { listener.handleUpdated(any(), any()) }
    }

}

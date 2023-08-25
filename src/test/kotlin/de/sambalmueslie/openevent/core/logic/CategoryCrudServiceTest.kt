package de.sambalmueslie.openevent.core.logic

import de.sambalmueslie.openevent.core.logic.category.CategoryChangeListener
import de.sambalmueslie.openevent.core.logic.category.CategoryCrudService
import de.sambalmueslie.openevent.core.model.AccountChangeRequest
import de.sambalmueslie.openevent.core.model.Category
import de.sambalmueslie.openevent.core.model.CategoryChangeRequest
import de.sambalmueslie.openevent.core.storage.AccountStorage
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.*
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@MicronautTest
class CategoryCrudServiceTest {

    @Inject
    lateinit var accountStorage: AccountStorage

    @Inject
    lateinit var service: CategoryCrudService

    @Test
    fun categoryCrud() {
        val actor = accountStorage.create(AccountChangeRequest("user", "first", "last", "email@localhost", "", ""))

        val listener = mockk<CategoryChangeListener>()
        service.register(listener)
        every { listener.handleCreated(any(), any()) } just Runs
        every { listener.handleUpdated(any(), any()) } just Runs
        every { listener.handleDeleted(any(), any()) } just Runs

        val request = CategoryChangeRequest("name", "icon-url")
        var category = service.create(actor, request)
        verify { listener.handleCreated(actor, category) }

        assertEquals(request.name, category.name)
        assertEquals(request.iconUrl, category.iconUrl)
        assertTrue(category.id > 0)

        assertEquals(category, service.get(category.id))
        assertEquals(category, service.findByName(request.name))
        assertEquals(listOf(category), service.getAll(Pageable.from(0)).content)

        val update = CategoryChangeRequest("name-update", "icon-url-update")
        category = service.update(actor, category.id, update)
        verify { listener.handleUpdated(actor, category) }

        assertEquals(update.name, category.name)
        assertEquals(update.iconUrl, category.iconUrl)

        service.delete(actor, category.id)
        verify { listener.handleDeleted(actor, category) }
        assertEquals(emptyList<Category>(), service.getAll(Pageable.from(0)).content)

    }

}

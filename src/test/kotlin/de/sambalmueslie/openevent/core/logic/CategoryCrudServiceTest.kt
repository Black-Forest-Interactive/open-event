package de.sambalmueslie.openevent.core.logic

import de.sambalmueslie.openevent.core.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.logic.category.CategoryCrudService
import de.sambalmueslie.openevent.core.model.Category
import de.sambalmueslie.openevent.core.model.CategoryChangeRequest
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
    lateinit var service: CategoryCrudService

    @Test
    fun categoryCrud() {
        val listener = mockk<BusinessObjectChangeListener<Long, Category>>()
        service.register(listener)
        every { listener.handleCreated(any()) } just Runs
        every { listener.handleUpdated(any()) } just Runs
        every { listener.handleDeleted(any()) } just Runs

        val request = CategoryChangeRequest("name", "icon-url")
        var category = service.create(request)
        verify { listener.handleCreated(category) }

        assertEquals(request.name, category.name)
        assertEquals(request.iconUrl, category.iconUrl)
        assertTrue(category.id > 0)

        assertEquals(category, service.get(category.id))
        assertEquals(category, service.findByName(request.name))
        assertEquals(listOf(category), service.getAll(Pageable.from(0)).content)

        val update = CategoryChangeRequest("name-update", "icon-url-update")
        category = service.update(category.id, update)
        verify { listener.handleUpdated(category) }

        assertEquals(update.name, category.name)
        assertEquals(update.iconUrl, category.iconUrl)

        service.delete(category.id)
        verify { listener.handleDeleted(category) }
        assertEquals(emptyList<Category>(), service.getAll(Pageable.from(0)).content)

    }

}

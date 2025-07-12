package de.sambalmueslie.openevent.core.logic.category

import de.sambalmueslie.openevent.TimeBasedTest
import de.sambalmueslie.openevent.core.account.AccountStorage
import de.sambalmueslie.openevent.core.account.api.AccountChangeRequest
import de.sambalmueslie.openevent.core.category.CategoryChangeListener
import de.sambalmueslie.openevent.core.category.CategoryCrudService
import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.core.category.api.CategoryChangeRequest
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.*
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@MicronautTest
class CategoryCrudServiceTest : TimeBasedTest() {

    @Inject
    lateinit var accountStorage: AccountStorage

    @Inject
    lateinit var service: CategoryCrudService

    @Test
    fun categoryCrud() {
        val actor = accountStorage.create(AccountChangeRequest("user", "", "actor-id"))

        val listener = mockk<CategoryChangeListener>()
        service.register(listener)
        every { listener.handleCreated(any(), any()) } just Runs
        every { listener.handleUpdated(any(), any()) } just Runs
        every { listener.handleDeleted(any(), any()) } just Runs

        val request = CategoryChangeRequest("name", "icon-url")
        var category = service.create(actor, request)
        verify { listener.handleCreated(actor, category) }

        Assertions.assertEquals(request.name, category.name)
        Assertions.assertEquals(request.iconUrl, category.iconUrl)
        Assertions.assertTrue(category.id > 0)

        Assertions.assertEquals(category, service.get(category.id))
        Assertions.assertEquals(category, service.findByName(request.name))
        Assertions.assertEquals(listOf(category), service.getAll(Pageable.from(0)).content)

        val update = CategoryChangeRequest("name-update", "icon-url-update")
        category = service.update(actor, category.id, update)
        verify { listener.handleUpdated(actor, category) }

        Assertions.assertEquals(update.name, category.name)
        Assertions.assertEquals(update.iconUrl, category.iconUrl)

        service.delete(actor, category.id)
        verify { listener.handleDeleted(actor, category) }
        Assertions.assertEquals(emptyList<Category>(), service.getAll(Pageable.from(0)).content)

    }

}
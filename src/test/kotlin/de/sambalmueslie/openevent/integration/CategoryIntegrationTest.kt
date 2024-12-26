package de.sambalmueslie.openevent.integration

import de.sambalmueslie.openevent.TimeBasedTest
import de.sambalmueslie.openevent.api.AccountAPI
import de.sambalmueslie.openevent.api.CategoryAPI.Companion.PERMISSION_READ
import de.sambalmueslie.openevent.api.CategoryAPI.Companion.PERMISSION_WRITE
import de.sambalmueslie.openevent.core.account.AccountController
import de.sambalmueslie.openevent.core.category.CategoryController
import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.core.category.api.CategoryChangeRequest
import de.sambalmueslie.openevent.error.InsufficientPermissionsException
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@MicronautTest
class CategoryIntegrationTest : TimeBasedTest() {
    @Inject
    lateinit var controller: CategoryController

    @Inject
    lateinit var accountController: AccountController

    @Test
    fun testCategoryCrudEndpoints() {
        val readWrite = getAuthentication(
            "test1@localhost",
            PERMISSION_WRITE,
            PERMISSION_READ,
            AccountAPI.PERMISSION_READ
        )
        val readOnly = getAuthentication(
            "test2@localhost",
            PERMISSION_READ,
            AccountAPI.PERMISSION_READ
        )
        val noAccess = getAuthentication(
            "test2@localhost",
            AccountAPI.PERMISSION_READ
        )
        accountController.validate(readWrite)
        accountController.validate(readOnly)
        accountController.validate(noAccess)

        val request = CategoryChangeRequest("name", "icon-url")
        var response = controller.create(readWrite, request)
        assertEquals(Category(response.id, request.name, request.iconUrl), response)

        assertEquals(response, controller.get(readWrite, response.id))
        assertEquals(response, controller.findByName(readWrite, request.name))
        assertEquals(listOf(response), controller.getAll(readWrite, Pageable.from(0)).content)

        val update = CategoryChangeRequest("name-update", "icon-url-update")
        response = controller.update(readWrite, response.id, update)
        assertEquals(Category(response.id, update.name, update.iconUrl), response)


        assertEquals(response, controller.get(readOnly, response.id))
        assertEquals(response, controller.findByName(readOnly, update.name))
        assertEquals(listOf(response), controller.getAll(readOnly, Pageable.from(0)).content)
        assertThrows<InsufficientPermissionsException> { controller.create(readOnly, request) }
        assertThrows<InsufficientPermissionsException> { controller.update(readOnly, response.id, update) }
        assertThrows<InsufficientPermissionsException> { controller.delete(readOnly, response.id) }

        assertThrows<InsufficientPermissionsException> { controller.get(noAccess, response.id) }
        assertThrows<InsufficientPermissionsException> { controller.findByName(noAccess, request.name) }
        assertThrows<InsufficientPermissionsException> { controller.getAll(noAccess, Pageable.from(0)) }
        assertThrows<InsufficientPermissionsException> { controller.create(noAccess, request) }
        assertThrows<InsufficientPermissionsException> { controller.update(noAccess, response.id, update) }
        assertThrows<InsufficientPermissionsException> { controller.delete(noAccess, response.id) }

        controller.delete(readWrite, response.id)
        assertEquals(emptyList<Category>(), controller.getAll(readWrite, Pageable.from(0)).content)
    }

}
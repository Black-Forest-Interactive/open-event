package de.sambalmueslie.openevent.api

import de.sambalmueslie.openevent.core.address.api.Address
import de.sambalmueslie.openevent.core.address.api.AddressChangeRequest
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication

interface AddressAPI : CrudAPI<Long, Address, AddressChangeRequest> {
    companion object {
        const val PERMISSION_READ = "openevent.address.read"
        const val PERMISSION_WRITE = "openevent.address.write"
        const val PERMISSION_ADMIN = "openevent.address.admin"
    }

    fun importLocations(auth: Authentication): Page<Address>
    fun getForAccount(auth: Authentication, accountId: Long, pageable: Pageable): Page<Address>
}

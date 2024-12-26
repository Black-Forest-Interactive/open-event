package de.sambalmueslie.openevent.core.address.db

import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.address.api.Address
import de.sambalmueslie.openevent.core.address.api.AddressChangeRequest
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable

interface AddressStorage : Storage<Long, Address, AddressChangeRequest> {
    fun create(request: AddressChangeRequest, account: Account): Address
    fun findByAccount(account: Account, pageable: Pageable): Page<Address>
}

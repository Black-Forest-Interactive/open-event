package de.sambalmueslie.openevent.core.address.db


import de.sambalmueslie.openevent.common.DataObjectRepository
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface AddressRepository : DataObjectRepository<Long, AddressData> {
    fun findByAccountId(accountId: Long, pageable: Pageable): Page<AddressData>
}

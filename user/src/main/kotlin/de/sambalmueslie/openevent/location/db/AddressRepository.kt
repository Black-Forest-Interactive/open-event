package de.sambalmueslie.openevent.location.db

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface AddressRepository : PageableRepository<AddressData, Long> {
    fun findByLocationId(locationId: Long): AddressData?
    fun deleteByLocationId(locationId: Long)
}

package de.sambalmueslie.openevent.core.event.contact

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface EventContactRepository : CrudRepository<EventContactData, Long>

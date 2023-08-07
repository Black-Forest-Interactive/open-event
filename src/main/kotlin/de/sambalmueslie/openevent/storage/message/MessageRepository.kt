package de.sambalmueslie.openevent.storage.message


import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface MessageRepository : PageableRepository<MessageData, Long> {
    fun findByIdIn(ids: Set<Long>): List<MessageData>
}

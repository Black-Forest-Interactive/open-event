package de.sambalmueslie.openevent.user.db

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface UserRepository : PageableRepository<UserData, Long> {
    fun findByEmail(email: String): List<UserData>

    fun findByExternalId(externalId: String): List<UserData>
}

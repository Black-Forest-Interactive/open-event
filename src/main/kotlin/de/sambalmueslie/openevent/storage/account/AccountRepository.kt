package de.sambalmueslie.openevent.storage.account


import de.sambalmueslie.openevent.storage.DataObjectRepository
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface AccountRepository : DataObjectRepository<Long, AccountData> {
    fun findByExternalId(externalId: String): AccountData?
    fun findByName(name: String, pageable: Pageable): Page<AccountData>
    fun findByEmail(email: String): AccountData?

}

package de.sambalmueslie.openevent.core.account.db


import de.sambalmueslie.openevent.common.DataObjectRepository
import io.micronaut.data.annotation.Query
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

    @Query("select a.* from account a inner join profile p on p.id = a.id WHERE p.email = :email LIMIT 1")
    fun findByEmail(email: String): AccountData?

}

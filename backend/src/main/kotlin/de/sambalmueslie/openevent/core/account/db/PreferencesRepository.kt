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
interface PreferencesRepository : DataObjectRepository<Long, PreferencesData> {

    @Query(
        value = "SELECT * FROM preferences WHERE (email::jsonb->>'enabled')::boolean = true",
        countQuery = "SELECT COUNT(*) FROM preferences WHERE (email::jsonb->>'enabled')::boolean = true"
    )
    fun findAllByEmailNotificationsEnabled(pageable: Pageable): Page<PreferencesData>

}

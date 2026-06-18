package de.sambalmueslie.openevent.core.audience.db


import de.sambalmueslie.openevent.common.DataObjectRepository
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface AudienceRepository : DataObjectRepository<Long, AudienceData> {
    fun findByName(name: String): AudienceData?
}

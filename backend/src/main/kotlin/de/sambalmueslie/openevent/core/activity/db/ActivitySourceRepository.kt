package de.sambalmueslie.openevent.core.activity.db

import de.sambalmueslie.openevent.common.DataObjectRepository
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface ActivitySourceRepository : DataObjectRepository<Long, ActivitySourceData> {
    fun findByKey(key: String): ActivitySourceData?
}
package de.sambalmueslie.openevent.core.share.db


import de.sambalmueslie.openevent.common.DataObjectRepository
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface ShareRepository : DataObjectRepository<String, ShareData> {
    fun findByEventId(eventId: Long): ShareData?
    fun findByEventIdIn(eventIds: Set<Long>): List<ShareData>
}

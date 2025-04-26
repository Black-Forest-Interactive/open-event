package de.sambalmueslie.openevent.core.notification.db

import de.sambalmueslie.openevent.common.DataObjectRepository
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface NotificationTypeRepository : DataObjectRepository<Long, NotificationTypeData> {
    fun findByKey(key: String): NotificationTypeData?
    fun findByKeyIn(keys: Set<String>): List<NotificationTypeData>
}

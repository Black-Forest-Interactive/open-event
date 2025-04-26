package de.sambalmueslie.openevent.core.notification.db

import de.sambalmueslie.openevent.common.DataObjectRepository
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface NotificationSettingRepository : DataObjectRepository<Long, NotificationSettingData> {
    fun findByName(name: String): NotificationSettingData?
}

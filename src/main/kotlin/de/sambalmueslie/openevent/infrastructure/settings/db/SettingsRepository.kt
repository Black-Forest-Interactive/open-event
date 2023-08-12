package de.sambalmueslie.openevent.infrastructure.settings.db

import de.sambalmueslie.openevent.storage.DataObjectRepository
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface SettingsRepository : DataObjectRepository<Long, SettingData> {
    fun findByKey(key: String): SettingData?
    fun findAllOrderByKey(pageable: Pageable): Page<SettingData>
    fun findAllOrderById(pageable: Pageable): Page<SettingData>

}

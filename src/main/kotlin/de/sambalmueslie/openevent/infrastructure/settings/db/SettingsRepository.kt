package de.sambalmueslie.openevent.infrastructure.settings.db

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface SettingsRepository : PageableRepository<SettingData, Long> {
    fun findByKey(key: String): SettingData?
    fun findAllOrderByKey(pageable: Pageable): Page<SettingData>
    fun findAllOrderById(pageable: Pageable): Page<SettingData>

}

package de.sambalmueslie.openevent.core.link.db

import de.sambalmueslie.openevent.common.DataObjectRepository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect

@JdbcRepository(dialect = Dialect.POSTGRES)
interface LinkRepository : DataObjectRepository<String, LinkData> {
    fun findByKey(key: String): LinkData?
    fun findByKeyIn(keys: Set<String>): List<LinkData>
}
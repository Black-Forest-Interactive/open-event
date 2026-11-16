package de.sambalmueslie.openevent.core.finder.db

import de.sambalmueslie.openevent.common.DataObjectRepository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect

@JdbcRepository(dialect = Dialect.POSTGRES)
interface FinderRepository : DataObjectRepository<Long, FinderData> {
    fun findByOwnerId(ownerId: Long, pageable: Pageable): Page<FinderData>
}
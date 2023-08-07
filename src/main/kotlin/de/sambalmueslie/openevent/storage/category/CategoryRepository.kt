package de.sambalmueslie.openevent.storage.category


import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface CategoryRepository : PageableRepository<CategoryData, Long> {
    fun findByName(name: String): CategoryData?
    fun findByIdIn(ids: Set<Long>): List<CategoryData>
}

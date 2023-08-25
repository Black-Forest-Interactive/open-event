package de.sambalmueslie.openevent.storage.notification

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.GenericRepository

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface TypeSchemeRelationRepository : GenericRepository<TypeSchemeRelation, Long> {

    fun findBySchemeId(schemeId: Long): List<TypeSchemeRelation>
    fun findByTypeId(typeId: Long): List<TypeSchemeRelation>
    fun findByTypeId(typeId: Long, pageable: Pageable): Page<TypeSchemeRelation>

    fun saveAll(data: List<TypeSchemeRelation>): List<TypeSchemeRelation>

    fun deleteBySchemeId(schemeId: Long)
    fun deleteByTypeId(typeId: Long)
}

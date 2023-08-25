package de.sambalmueslie.openevent.storage.notification

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.GenericRepository

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface SchemeSubscriberRelationRepository : GenericRepository<SchemeSubscriberRelation, Long> {

    fun findBySchemeId(schemaId: Long): List<SchemeSubscriberRelation>
    fun findBySchemeId(schemaId: Long, pageable: Pageable): Page<SchemeSubscriberRelation>

    fun saveAll(data: List<SchemeSubscriberRelation>): List<SchemeSubscriberRelation>
    fun save(data: SchemeSubscriberRelation): SchemeSubscriberRelation

    fun deleteBySchemeId(schemeId: Long)
    fun deleteByAccountId(accountId: Long)
    fun deleteBySchemeIdAndAccountId(schemeId: Long, accountId: Long)
    fun existsBySchemeIdAndAccountId(schemeId: Long, accountId: Long): Boolean
    fun findByAccountId(id: Long): List<SchemeSubscriberRelation>
}

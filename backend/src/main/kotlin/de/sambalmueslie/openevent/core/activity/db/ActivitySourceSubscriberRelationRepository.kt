package de.sambalmueslie.openevent.core.activity.db

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.GenericRepository

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface ActivitySourceSubscriberRelationRepository : GenericRepository<ActivitySourceSubscriberRelation, Long> {
    fun save(relation: ActivitySourceSubscriberRelation): ActivitySourceSubscriberRelation
    fun saveAll(relations: List<ActivitySourceSubscriberRelation>): List<ActivitySourceSubscriberRelation>

    fun findBySourceIdAndAccountIdIn(sourceId: Long, accountIds: Set<Long>): List<ActivitySourceSubscriberRelation>
}

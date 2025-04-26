package de.sambalmueslie.openevent.core.activity.db

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.GenericRepository

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface ActivityTypeSubscriberRelationRepository : GenericRepository<ActivityTypeSubscriberRelation, Long> {
    fun save(relation: ActivityTypeSubscriberRelation): ActivityTypeSubscriberRelation
    fun saveAll(relations: List<ActivityTypeSubscriberRelation>): List<ActivityTypeSubscriberRelation>
    fun findByTypeIdAndAccountIdIn(typeId: Long, accountIds: Set<Long>): List<ActivityTypeSubscriberRelation>
}

package de.sambalmueslie.openevent.core.activity.db

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.jdbc.runtime.JdbcOperations
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.GenericRepository

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface ActivitySubscriberRelationRepository :
    GenericRepository<ActivitySubscriberRelation, Long> {
     fun save(relation: ActivitySubscriberRelation): ActivitySubscriberRelation
     fun saveAll(relations: List<ActivitySubscriberRelation>): List<ActivitySubscriberRelation>

     fun findByAccountIdOrderByTimestamp(accountId: Long, pageable: Pageable): Page<ActivitySubscriberRelation>
     fun findByAccountIdAndReadFalseOrderByTimestamp(
        accountId: Long,
        pageable: Pageable
    ): Page<ActivitySubscriberRelation>

     fun findByActivityIdAndAccountId(activityId: Long, accountId: Long): ActivitySubscriberRelation?
    fun updateByActivityIdAndAccountId(activityId: Long, accountId: Long, read: Boolean)

}

package de.sambalmueslie.openevent.core.activity.db

import de.sambalmueslie.openevent.common.DataObjectRepository
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import java.time.LocalDateTime

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface ActivityRepository : DataObjectRepository<Long, ActivityData> {

    @Query(
        value = "SELECT a.* FROM activity a INNER JOIN activity_subscriber s ON a.id = s.activity_id WHERE a.actor_id != :accountId and s.account_id = :accountId ORDER BY a.created DESC",
        countQuery = "SELECT COUNT(*) FROM activity a INNER JOIN activity_subscriber s ON a.id = s.activity_id WHERE a.actor_id != :accountId and s.account_id = :accountId"
    )
    fun findRecentForAccount(accountId: Long, pageable: Pageable): Page<ActivityData>

    fun findByCreatedLessThan(timestamp: LocalDateTime, pageable: Pageable): Page<ActivityData>
    fun deleteByIdIn(ids: Set<Long>)
}
package de.sambalmueslie.openevent.core.feedback.db

import de.sambalmueslie.openevent.common.DataObjectRepository
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface FeedbackRepository : DataObjectRepository<Long, FeedbackData> {
    fun findByAccountId(accountId: Long, pageable: Pageable): Page<FeedbackData>
    fun findByTopic(topic: String, pageable: Pageable): Page<FeedbackData>
}
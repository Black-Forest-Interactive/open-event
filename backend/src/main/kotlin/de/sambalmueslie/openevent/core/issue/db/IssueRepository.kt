package de.sambalmueslie.openevent.core.issue.db


import de.sambalmueslie.openevent.common.DataObjectRepository
import de.sambalmueslie.openevent.core.issue.api.IssueStatus
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface IssueRepository : DataObjectRepository<Long, IssueData> {
    fun findByAccountId(accountId: Long, pageable: Pageable): Page<IssueData>
    fun findByStatusIn(status: Set<IssueStatus>, pageable: Pageable): Page<IssueData>
    fun findByStatusInAndAccountId(status: Set<IssueStatus>, accountId: Long, pageable: Pageable): Page<IssueData>
}

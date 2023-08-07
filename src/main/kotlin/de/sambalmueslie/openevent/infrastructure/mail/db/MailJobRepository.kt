package de.sambalmueslie.openevent.infrastructure.mail.db

import de.sambalmueslie.openevent.infrastructure.mail.api.MailJobStatus
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository


@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface MailJobRepository : PageableRepository<MailJobData, Long> {

    fun findAllOrderByUpdatedDesc(pageable: Pageable): Page<MailJobData>
    fun findAllByStatus(status: MailJobStatus, pageable: Pageable): Page<MailJobData>

}

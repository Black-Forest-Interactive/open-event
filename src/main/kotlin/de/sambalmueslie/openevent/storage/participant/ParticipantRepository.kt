package de.sambalmueslie.openevent.storage.participant


import de.sambalmueslie.openevent.storage.DataObjectRepository
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface ParticipantRepository : DataObjectRepository<Long, ParticipantData> {

    fun findByRegistrationId(id: Long): List<ParticipantData>
    fun findByRegistrationIdAndAccountId(registrationId: Long, accountId: Long): ParticipantData?
    fun findByRegistrationIdIn(regIds: Set<Long>): List<ParticipantData>
}

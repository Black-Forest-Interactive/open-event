package de.sambalmueslie.openevent.core.finder.db

import de.sambalmueslie.openevent.core.finder.api.FinderAnswer
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository

@JdbcRepository(dialect = Dialect.POSTGRES)
interface FinderEntryRepository : PageableRepository<FinderEntryData, FinderEntryId> {
    fun findByIdFinderId(finderId: Long): List<FinderEntryData>
    fun findByIdFinderId(finderId: Long, pageable: Pageable): Page<FinderEntryData>
    fun findByIdFinderIdAndAnswer(finderId: Long, answer: FinderAnswer, pageable: Pageable): Page<FinderEntryData>
    fun findByIdFinderIdAndIdEventIdIn(finderId: Long, eventIds: Set<Long>): List<FinderEntryData>

    fun countByIdFinderIdAndAnswer(finderId: Long, answer: FinderAnswer): Long

    fun deleteByIdFinderId(finderId: Long)
    fun deleteByIdEventId(eventId: Long)
}

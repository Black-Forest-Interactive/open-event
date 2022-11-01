package de.sambalmueslie.openevent.group.db

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.GenericRepository

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface GroupUserRelationRepository : GenericRepository<GroupUserRelation, Long> {
    fun findByUserId(userId: Long, pageable: Pageable): Page<GroupUserRelation>
    fun findByGroupId(groupId: Long, pageable: Pageable): Page<GroupUserRelation>

    fun save(data: GroupUserRelation): GroupUserRelation
    fun existsByGroupIdAndUserId(groupId: Long, userId: Long): Boolean

    fun deleteByUserId(userId: Long)
    fun deleteByGroupId(groupId: Long)
    fun deleteByGroupIdAndUserId(groupId: Long, userId: Long)
}

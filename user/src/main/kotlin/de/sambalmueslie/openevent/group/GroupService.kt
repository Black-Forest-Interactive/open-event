package de.sambalmueslie.openevent.group


import de.sambalmueslie.openevent.common.crud.GenericCrudService
import de.sambalmueslie.openevent.common.error.InvalidRequestException
import de.sambalmueslie.openevent.common.time.TimeProvider
import de.sambalmueslie.openevent.group.api.Group
import de.sambalmueslie.openevent.group.api.GroupChangeRequest
import de.sambalmueslie.openevent.group.db.GroupData
import de.sambalmueslie.openevent.group.db.GroupRepository
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class GroupService(
    private val repository: GroupRepository,
    private val timeProvider: TimeProvider
) : GenericCrudService<Long, Group, GroupChangeRequest, GroupData>(repository, logger) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(GroupService::class.java)
    }


    override fun create(request: GroupChangeRequest): Group {
        val existing = repository.findByName(request.name)
        if (existing.isNotEmpty()) throw InvalidRequestException("Group name is already existing ${existing.size} times")
        return super.create(request)
    }

    override fun createData(request: GroupChangeRequest) = GroupData.create(request, timeProvider.now())
    override fun updateData(data: GroupData, request: GroupChangeRequest) = data.update(request, timeProvider.now())

    override fun isValid(request: GroupChangeRequest) {
        if (request.name.isBlank()) throw InvalidRequestException("Group name could not be blank")
    }


}

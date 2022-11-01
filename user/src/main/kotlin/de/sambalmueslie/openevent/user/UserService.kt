package de.sambalmueslie.openevent.user


import de.sambalmueslie.openevent.common.crud.GenericCrudService
import de.sambalmueslie.openevent.common.error.InvalidRequestException
import de.sambalmueslie.openevent.common.time.TimeProvider
import de.sambalmueslie.openevent.user.api.User
import de.sambalmueslie.openevent.user.api.UserChangeRequest
import de.sambalmueslie.openevent.user.db.UserData
import de.sambalmueslie.openevent.user.db.UserRepository
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class UserService(
    private val repository: UserRepository,
    private val timeProvider: TimeProvider
) : GenericCrudService<Long, User, UserChangeRequest, UserData>(repository, logger) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(UserService::class.java)
    }


    override fun create(request: UserChangeRequest): User {
        val existing = repository.findByEmail(request.email)
        if (existing.isNotEmpty()) throw InvalidRequestException("User email is already existing ${existing.size} times")
        return super.create(request)
    }

    override fun createData(request: UserChangeRequest) = UserData.create(request, timeProvider.now())
    override fun updateData(data: UserData, request: UserChangeRequest) = data.update(request, timeProvider.now())

    override fun isValid(request: UserChangeRequest) {
        if (request.email.isBlank()) throw InvalidRequestException("User email could not be blank")
        if (request.firstName.isBlank()) throw InvalidRequestException("User first name could not be blank")
        if (request.lastName.isBlank()) throw InvalidRequestException("User last name could not be blank")
    }

}

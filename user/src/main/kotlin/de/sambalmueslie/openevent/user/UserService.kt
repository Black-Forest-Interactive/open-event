package de.sambalmueslie.openevent.user


import de.sambalmueslie.openevent.common.crud.BaseCrudService
import de.sambalmueslie.openevent.common.error.InvalidRequestException
import de.sambalmueslie.openevent.common.time.TimeProvider
import de.sambalmueslie.openevent.common.util.findByIdOrNull
import de.sambalmueslie.openevent.user.api.User
import de.sambalmueslie.openevent.user.api.UserChangeRequest
import de.sambalmueslie.openevent.user.db.UserData
import de.sambalmueslie.openevent.user.db.UserRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class UserService(
    private val repository: UserRepository,
    private val timeProvider: TimeProvider
) : BaseCrudService<Long, User, UserChangeRequest>(logger) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(UserService::class.java)
    }

    override fun get(id: Long): User? {
        return repository.findByIdOrNull(id)?.convert()
    }

    override fun getAll(pageable: Pageable): Page<User> {
        return repository.findAll(pageable).map { it.convert() }
    }


    override fun create(request: UserChangeRequest): User {
        isValid(request)
        val existing = repository.findByEmail(request.email)
        if (existing.isNotEmpty()) throw InvalidRequestException("User email is already existing ${existing.size} times")

        val data = repository.save(UserData.create(request, timeProvider.now())).convert()
        notifyCreated(data)
        return data
    }

    override fun update(id: Long, request: UserChangeRequest): User {
        val data = repository.findByIdOrNull(id) ?: return create(request)
        isValid(request)
        val result = repository.update(data.update(request, timeProvider.now())).convert()
        notifyUpdated(result)
        return result
    }

    override fun delete(id: Long): User? {
        val data = repository.findByIdOrNull(id) ?: return null
        repository.delete(data)
        val result = data.convert()
        notifyDeleted(result)
        return result
    }

    private fun isValid(request: UserChangeRequest) {
        if (request.email.isBlank()) throw InvalidRequestException("User email could not be blank")
        if (request.firstName.isBlank()) throw InvalidRequestException("User first name could not be blank")
        if (request.lastName.isBlank()) throw InvalidRequestException("User last name could not be blank")
    }

}

package de.sambalmueslie.openevent.core.participant


import de.sambalmueslie.openevent.common.findByIdOrNull
import de.sambalmueslie.openevent.config.AppConfig
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountChangeRequest
import de.sambalmueslie.openevent.core.account.api.AccountSetupRequest
import de.sambalmueslie.openevent.core.account.api.ProfileChangeRequest
import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.core.notification.handler.ExternalParticipantNotificationHandler
import de.sambalmueslie.openevent.core.participant.api.*
import de.sambalmueslie.openevent.core.participant.db.ExternalParticipantData
import de.sambalmueslie.openevent.core.participant.db.ExternalParticipantRepository
import de.sambalmueslie.openevent.core.registration.RegistrationCrudService
import de.sambalmueslie.openevent.core.registration.api.RegistrationInfo
import de.sambalmueslie.openevent.core.share.api.Share
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.settings.SettingsService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.time.Duration
import java.time.format.DateTimeParseException
import java.util.*
import kotlin.random.Random

@Singleton
class ExternalParticipantService(
    private val accountService: AccountCrudService,
    private val settingsService: SettingsService,
    private val registrationService: RegistrationCrudService,
    private val notificationHandler: ExternalParticipantNotificationHandler,

    private val repository: ExternalParticipantRepository,
    private val timeProvider: TimeProvider,
    private val config: AppConfig
) {

    companion object {
        private val logger = LoggerFactory.getLogger(ExternalParticipantService::class.java)
        private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    }

    private val expires = try {
        Duration.parse(config.externalParticipantExpires)
    } catch (e: DateTimeParseException) {
        Duration.ofHours(24)
    }
    private val systemAccount = accountService.getSystemAccount()

    fun requestParticipation(share: Share, event: EventInfo, request: ExternalParticipantAddRequest, lang: String): ExternalParticipantChangeResponse {
        val registration = event.registration ?: return ExternalParticipantChangeResponse.failed()
        validate(request)
        val existing = repository.findByEventIdAndEmail(event.event.id, request.email)
        val result = if (existing != null) {
            requestParticipationForExisting(event, existing, request)
        } else {
            requestParticipationNew(event, request, lang)
        } ?: return ExternalParticipantChangeResponse.failed()
        notificationHandler.handleCreated(systemAccount, share, event, registration, result)
        return ExternalParticipantChangeResponse(ParticipateStatus.UNCONFIRMED)
    }


    private fun requestParticipationNew(event: EventInfo, request: ExternalParticipantAddRequest, lang: String): ExternalParticipantData? {
        val id = UUID.randomUUID().toString()
        val now = timeProvider.now()
        val expiresTimestamp = now.plus(expires)
        val code = Random.nextInt(10000, 100000).toString().format("%05d")
        return repository.save(ExternalParticipantData.create(id, event, request, lang.ifBlank { settingsService.getLanguage() }, code, expiresTimestamp, now))
    }

    private fun requestParticipationForExisting(event: EventInfo, existing: ExternalParticipantData, request: ExternalParticipantAddRequest): ExternalParticipantData? {
        val now = timeProvider.now()
        val expiresTimestamp = now.plus(expires)
        return repository.update(existing.updateExpires(expiresTimestamp, now))
    }


    private fun validate(request: ExternalParticipantAddRequest) {
        if (request.email.isBlank()) throw InvalidRequestException("E-Mail cannot be blank")
        if (request.firstName.isBlank()) throw InvalidRequestException("First name cannot be blank")
        if (request.lastName.isBlank()) throw InvalidRequestException("Last name cannot be blank")
        if (request.size <= 0) throw InvalidRequestException("Size must be a positive number")
        val validEmail = emailRegex.matches(request.email)
        if (!validEmail) throw InvalidRequestException("Provided E-Mail is invalid")
    }

    fun changeParticipation(event: EventInfo, participantId: String, request: ExternalParticipantChangeRequest): ExternalParticipantChangeResponse {
        TODO("Not yet implemented")
    }

    fun cancelParticipation(event: EventInfo, participantId: String): ExternalParticipantChangeResponse {
        TODO("Not yet implemented")
    }

    fun confirmParticipation(event: EventInfo, participantId: String, request: ExternalParticipantConfirmRequest): ExternalParticipantConfirmResponse {
        val registration = event.registration ?: return ExternalParticipantConfirmResponse.failed()
        val participant = repository.findByIdOrNull(participantId) ?: return ExternalParticipantConfirmResponse.failed()
        val eventValid = participant.eventId == event.event.id
        if (!eventValid) return ExternalParticipantConfirmResponse.failed()

        val codeValid = request.code == participant.code
        return if (!codeValid) {
            handleInvalidConfirmation(participant)
        } else {
            handleValidConfirmation(event, registration, participant)
        }
    }

    private fun handleInvalidConfirmation(participant: ExternalParticipantData): ExternalParticipantConfirmResponse {
        if (participant.invalidConfirmationTrials >= config.maxConfirmationTrials) {
            repository.delete(participant)
        } else {
            participant.invalidConfirmationTrials++
            repository.update(participant)
        }
        return ExternalParticipantConfirmResponse.failed()
    }

    private fun handleValidConfirmation(event: EventInfo, registration: RegistrationInfo, participant: ExternalParticipantData): ExternalParticipantConfirmResponse {
        val (_, account) = getOrCreateAccount(participant) ?: return ExternalParticipantConfirmResponse.failed()
        if (registration.participants.any { it.author.id == account.id }) return ExternalParticipantConfirmResponse.failed()

        val participateRequest = ParticipateRequest(participant.size)
        val participateResponse = registrationService.addParticipant(systemAccount, registration.registration.id, account, participateRequest) ?: return ExternalParticipantConfirmResponse.failed()
        repository.delete(participant)
        return ExternalParticipantConfirmResponse(participateResponse.participant?.toExternalParticipant(), participateResponse.status)
    }

    private fun getOrCreateAccount(data: ExternalParticipantData): Pair<Boolean, Account>? {
        val account = accountService.findByEmail(data.email)
        if (account != null) return Pair(false, account)


        val name = "${data.firstName} ${data.lastName}"

        val accountRequest = AccountSetupRequest(
            AccountChangeRequest(name, "", null),
            ProfileChangeRequest(data.email, data.phone, data.mobile, data.firstName, data.lastName, language = data.language),
        )
        val info = accountService.setup(systemAccount, accountRequest)
        val createdAccount = accountService.get(info.id) ?: return null
        return Pair(true, createdAccount)
    }

}
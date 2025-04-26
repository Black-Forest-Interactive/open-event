package de.sambalmueslie.openevent.core.participant.db


import de.sambalmueslie.openevent.common.DataObjectConverter
import de.sambalmueslie.openevent.core.account.api.AccountDetails
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.account.db.AccountStorageService
import de.sambalmueslie.openevent.core.participant.api.Participant
import de.sambalmueslie.openevent.core.participant.api.ParticipantDetails
import de.sambalmueslie.openevent.error.InconsistentDataException
import io.micronaut.data.model.Page
import jakarta.inject.Singleton

@Singleton
class ParticipantDetailsConverter(
    private val accountService: AccountStorageService,
) : DataObjectConverter<ParticipantDetails, ParticipantData> {

    override fun convert(obj: ParticipantData): ParticipantDetails {
        return convert(obj, accountService.getDetails(obj.accountId))
    }

    override fun convert(objs: List<ParticipantData>): List<ParticipantDetails> {
        val authorIds = objs.map { it.accountId }.toSet()
        val author = accountService.getDetailsByIds(authorIds).associateBy { it.id }
        return objs.map { convert(it, author[it.accountId]) }
    }

    override fun convert(page: Page<ParticipantData>): Page<ParticipantDetails> {
        val authorIds = page.content.map { it.accountId }.toSet()
        val author = accountService.getDetailsByIds(authorIds).associateBy { it.id }
        return page.map { convert(it, author[it.accountId]) }
    }

    private fun convert(data: ParticipantData, author: AccountDetails?): ParticipantDetails {
        if (author == null) throw InconsistentDataException("Cannot find author for participant")
        return data.convert(author)
    }
}

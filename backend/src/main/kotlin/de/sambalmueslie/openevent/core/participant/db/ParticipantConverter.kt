package de.sambalmueslie.openevent.core.participant.db


import de.sambalmueslie.openevent.common.DataObjectConverter
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.account.db.AccountStorageService
import de.sambalmueslie.openevent.core.participant.api.Participant
import de.sambalmueslie.openevent.error.InconsistentDataException
import io.micronaut.data.model.Page
import jakarta.inject.Singleton

@Singleton
class ParticipantConverter(
    private val accountService: AccountStorageService,
) : DataObjectConverter<Participant, ParticipantData> {

    override fun convert(obj: ParticipantData): Participant {
        return convert(obj, accountService.getInfo(obj.accountId))
    }

    override fun convert(objs: List<ParticipantData>): List<Participant> {
        val authorIds = objs.map { it.accountId }.toSet()
        val author = accountService.getInfoByIds(authorIds).associateBy { it.id }
        return objs.map { convert(it, author[it.accountId]) }
    }

    override fun convert(page: Page<ParticipantData>): Page<Participant> {
        val authorIds = page.content.map { it.accountId }.toSet()
        val author = accountService.getInfoByIds(authorIds).associateBy { it.id }
        return page.map { convert(it, author[it.accountId]) }
    }

    private fun convert(data: ParticipantData, author: AccountInfo?): Participant {
        if (author == null) throw InconsistentDataException("Cannot find author for participant")
        return data.convert(author)
    }
}

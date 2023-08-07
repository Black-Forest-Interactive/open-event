package de.sambalmueslie.openevent.storage.participant


import de.sambalmueslie.openevent.core.model.Account
import de.sambalmueslie.openevent.core.model.Participant
import de.sambalmueslie.openevent.error.InconsistentDataException
import de.sambalmueslie.openevent.storage.DataObjectConverter
import de.sambalmueslie.openevent.storage.account.AccountStorageService
import io.micronaut.data.model.Page
import jakarta.inject.Singleton

@Singleton
class ParticipantConverter(
    private val accountService: AccountStorageService,
) : DataObjectConverter<Participant, ParticipantData> {

    override fun convert(obj: ParticipantData): Participant {
        return convert(obj, accountService.get(obj.accountId))
    }

    override fun convert(objs: List<ParticipantData>): List<Participant> {
        val authorIds = objs.map { it.accountId }.toSet()
        val author = accountService.getByIds(authorIds).associateBy { it.id }
        return objs.map { convert(it, author[it.accountId]) }
    }

    override fun convert(page: Page<ParticipantData>): Page<Participant> {
        val authorIds = page.content.map { it.accountId }.toSet()
        val author = accountService.getByIds(authorIds).associateBy { it.id }
        return page.map { convert(it, author[it.accountId]) }
    }

    private fun convert(data: ParticipantData, author: Account?): Participant {
        if (author == null) throw InconsistentDataException("Cannot find author for participant")
        return data.convert(author)
    }
}

package de.sambalmueslie.openevent.storage.history


import de.sambalmueslie.openevent.core.model.Account
import de.sambalmueslie.openevent.core.model.HistoryEntry
import de.sambalmueslie.openevent.error.InconsistentDataException
import de.sambalmueslie.openevent.storage.DataObjectConverter
import de.sambalmueslie.openevent.storage.account.AccountStorageService
import io.micronaut.data.model.Page
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class HistoryEntryConverter(
    private val accountService: AccountStorageService,
) : DataObjectConverter<HistoryEntry, HistoryEntryData> {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(HistoryEntryConverter::class.java)
    }

    override fun convert(obj: HistoryEntryData): HistoryEntry {
        return convert(obj, accountService.get(obj.actorId))
    }

    override fun convert(objs: List<HistoryEntryData>): List<HistoryEntry> {
        val actorIds = objs.map { it.actorId }.toSet()
        val actor = accountService.getByIds(actorIds).associateBy { it.id }
        return objs.map { convert(it, actor[it.actorId]) }
    }

    override fun convert(page: Page<HistoryEntryData>): Page<HistoryEntry> {
        val actorIds = page.content.map { it.actorId }.toSet()
        val actor = accountService.getByIds(actorIds).associateBy { it.id }
        return page.map { convert(it, actor[it.actorId]) }
    }

    private fun convert(data: HistoryEntryData, actor: Account?): HistoryEntry {
        if (actor == null) throw InconsistentDataException("Cannot find actor for history entry")
        return data.convert(actor)
    }
}

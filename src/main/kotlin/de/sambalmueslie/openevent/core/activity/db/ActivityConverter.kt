package de.sambalmueslie.openevent.core.activity.db

import de.sambalmueslie.openevent.common.DataObjectConverter
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.account.db.AccountStorageService
import de.sambalmueslie.openevent.core.activity.api.Activity
import de.sambalmueslie.openevent.error.InconsistentDataException
import io.micronaut.data.model.Page
import jakarta.inject.Singleton

@Singleton
class ActivityConverter(
    private val accountService: AccountStorageService,
) : DataObjectConverter<Activity, ActivityData> {
    override fun convert(obj: ActivityData): Activity {
        return convert(obj, accountService.getInfo(obj.actorId))
    }

    override fun convert(objs: List<ActivityData>): List<Activity> {
        val actorIds = objs.map { it.actorId }.toSet()
        val actors = accountService.getInfoByIds(actorIds).associateBy { it.id }
        return objs.map { convert(it, actors[it.actorId]) }
    }

    override fun convert(page: Page<ActivityData>): Page<Activity> {
        val actorIds = page.content.map { it.actorId }.toSet()
        val actors = accountService.getInfoByIds(actorIds).associateBy { it.id }
        return page.map { convert(it, actors[it.actorId]) }
    }

    private fun convert(data: ActivityData, actor: AccountInfo?): Activity {
        if (actor == null) throw InconsistentDataException("Cannot find author for event")
        return data.convert(actor)
    }
}
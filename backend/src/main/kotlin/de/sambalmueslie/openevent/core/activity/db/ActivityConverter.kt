package de.sambalmueslie.openevent.core.activity.db

import de.sambalmueslie.openevent.common.DataObjectConverter
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.account.db.AccountStorageService
import de.sambalmueslie.openevent.core.activity.ActivitySourceStorage
import de.sambalmueslie.openevent.core.activity.ActivityTypeStorage
import de.sambalmueslie.openevent.core.activity.api.Activity
import de.sambalmueslie.openevent.core.activity.api.ActivitySource
import de.sambalmueslie.openevent.core.activity.api.ActivityType
import de.sambalmueslie.openevent.error.InconsistentDataException
import io.micronaut.data.model.Page
import jakarta.inject.Singleton

@Singleton
class ActivityConverter(
    private val accountService: AccountStorageService,
    private val sourceStorage: ActivitySourceStorage,
    private val typeStorage: ActivityTypeStorage,
) : DataObjectConverter<Activity, ActivityData> {
    override fun convert(obj: ActivityData): Activity {
        return convert(obj, accountService.getInfo(obj.actorId), sourceStorage.get(obj.sourceId), typeStorage.get(obj.typeId))
    }

    override fun convert(objs: List<ActivityData>): List<Activity> {
        val actorIds = objs.map { it.actorId }.toSet()
        val actors = accountService.getInfoByIds(actorIds).associateBy { it.id }
        val sourceIds = objs.map { it.sourceId }.toSet()
        val sources = sourceStorage.findByIds(sourceIds).associateBy { it.id }
        val typeIds = objs.map { it.typeId }.toSet()
        val types = typeStorage.findByIds(typeIds).associateBy { it.id }
        return objs.map { convert(it, actors[it.actorId], sources[it.sourceId], types[it.typeId]) }
    }

    override fun convert(page: Page<ActivityData>): Page<Activity> {
        val actorIds = page.content.map { it.actorId }.toSet()
        val actors = accountService.getInfoByIds(actorIds).associateBy { it.id }
        val sourceIds = page.content.map { it.sourceId }.toSet()
        val sources = sourceStorage.findByIds(sourceIds).associateBy { it.id }
        val typeIds = page.content.map { it.typeId }.toSet()
        val types = typeStorage.findByIds(typeIds).associateBy { it.id }
        return page.map { convert(it, actors[it.actorId], sources[it.sourceId], types[it.typeId]) }
    }

    private fun convert(data: ActivityData, actor: AccountInfo?, source: ActivitySource?, type: ActivityType?): Activity {
        if (actor == null) throw InconsistentDataException("Cannot find actor for activity")
        if (source == null) throw InconsistentDataException("Cannot find source for activity")
        if (type == null) throw InconsistentDataException("Cannot find type for activity")
        return data.convert(actor, source, type)
    }
}
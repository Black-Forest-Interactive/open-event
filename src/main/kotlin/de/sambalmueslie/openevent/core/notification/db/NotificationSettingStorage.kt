package de.sambalmueslie.openevent.core.notification.db

import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.notification.api.NotificationSetting
import de.sambalmueslie.openevent.core.notification.api.NotificationSettingChangeRequest

interface NotificationSettingStorage : Storage<Long, NotificationSetting, NotificationSettingChangeRequest> {
    fun setEnabled(id: Long, value: PatchRequest<Boolean>): NotificationSetting?
    fun findByName(name: String): NotificationSetting?
}

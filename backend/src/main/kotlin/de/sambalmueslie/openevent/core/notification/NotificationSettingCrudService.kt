package de.sambalmueslie.openevent.core.notification


import de.sambalmueslie.openevent.common.BaseCrudService
import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.notification.api.NotificationSetting
import de.sambalmueslie.openevent.core.notification.api.NotificationSettingChangeRequest
import de.sambalmueslie.openevent.core.notification.db.NotificationSettingStorage
import de.sambalmueslie.openevent.error.InvalidRequestException
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class NotificationSettingCrudService(
    private val storage: NotificationSettingStorage,
) : BaseCrudService<Long, NotificationSetting, NotificationSettingChangeRequest, NotificationSettingChangeListener>(
    storage
) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(NotificationSettingCrudService::class.java)
    }

    override fun isValid(request: NotificationSettingChangeRequest) {
        if (request.name.isBlank()) throw InvalidRequestException("Name cannot be blank.")
    }

    fun findByName(name: String): NotificationSetting? {
        return storage.findByName(name)
    }

    fun setEnabled(id: Long, value: PatchRequest<Boolean>): NotificationSetting? {
        return storage.setEnabled(id, value)
    }
}

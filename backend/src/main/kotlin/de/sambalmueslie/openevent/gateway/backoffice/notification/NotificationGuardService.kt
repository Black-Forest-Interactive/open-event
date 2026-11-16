package de.sambalmueslie.openevent.gateway.backoffice.notification

import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.notification.NotificationTypeCrudService
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class NotificationGuardService(
    private val typeService: NotificationTypeCrudService,
) {
    companion object {
        private const val PERMISSION_READ = "mail.read"
        private const val PERMISSION_WRITE = "mail.write"
        private const val PERMISSION_ADMIN = "mail.admin"
    }

    fun getTypes(auth: Authentication, pageable: Pageable) =
        auth.checkPermission(PERMISSION_READ, PERMISSION_ADMIN) { typeService.getAll(pageable) }

    fun setTypeEnabled(auth: Authentication, id: Long, value: PatchRequest<Boolean>) =
        auth.checkPermission(PERMISSION_WRITE, PERMISSION_ADMIN) { typeService.setEnabled(id, value) }
}

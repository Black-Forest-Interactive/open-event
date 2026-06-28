package de.sambalmueslie.openevent.gateway.backoffice.newsletter

import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.account.PreferencesCrudService
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.notification.NotificationSettingCrudService
import de.sambalmueslie.openevent.core.notification.api.NotificationSetting
import de.sambalmueslie.openevent.core.notification.handler.NewsletterNotificationHandler
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Put
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/newsletter")
@Tag(name = "BACKOFFICE Newsletter API")
class NewsletterController(
    private val settingService: NotificationSettingCrudService,
    private val preferencesService: PreferencesCrudService,
    private val accountService: AccountCrudService,
) {
    companion object {
        private const val PERMISSION_READ = "mail.read"
        private const val PERMISSION_WRITE = "mail.write"
        private const val PERMISSION_ADMIN = "mail.admin"
    }

    @Get("/subscriber")
    fun getSubscribers(auth: Authentication, pageable: Pageable): Page<AccountInfo> {
        return auth.checkPermission(PERMISSION_ADMIN) {
            val prefs = preferencesService.findByEmailNotificationsEnabled(pageable)
            val infos = prefs.content.mapNotNull { pref ->
                val acc = accountService.getById(pref.id) ?: return@mapNotNull null
                accountService.getInfo(acc)
            }
            Page.of(infos, prefs.pageable, prefs.totalSize)
        }
    }

    @Get("/setting")
    fun getSetting(auth: Authentication): NotificationSetting? {
        return auth.checkPermission(PERMISSION_READ, PERMISSION_ADMIN) {
            settingService.findByName(NewsletterNotificationHandler.SETTING_NAME)
        }
    }

    @Put("/setting/{id}/enabled")
    fun setSettingEnabled(auth: Authentication, id: Long, @Body value: PatchRequest<Boolean>): NotificationSetting? {
        return auth.checkPermission(PERMISSION_WRITE, PERMISSION_ADMIN) {
            settingService.setEnabled(id, value)
        }
    }
}

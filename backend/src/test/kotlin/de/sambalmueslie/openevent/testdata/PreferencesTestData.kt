package de.sambalmueslie.openevent.testdata

import de.sambalmueslie.openevent.core.account.PreferencesCrudService
import de.sambalmueslie.openevent.core.account.api.*

/**
 * Reusable test data for preferences. Like profiles, preferences are keyed by their account
 * (preferences.id == account.id), so the persist helper takes the owning [Account].
 */
object PreferencesTestData {

    fun createRequest(
        email: EmailNotificationsPreferences = EmailNotificationsPreferences(),
        communication: CommunicationPreferences = CommunicationPreferences(),
        notification: NotificationPreferences = NotificationPreferences(),
    ) = PreferencesChangeRequest(email, communication, notification)

    fun updateRequest(enabled: Boolean = true) = PreferencesChangeRequest(
        EmailNotificationsPreferences(enabled),
        CommunicationPreferences(enabled),
        NotificationPreferences(enabled),
    )

    fun create(
        service: PreferencesCrudService,
        actor: Account,
        account: Account,
        request: PreferencesChangeRequest = createRequest(),
    ): Preferences = service.create(actor, account, request)

}

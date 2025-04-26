export interface Preferences {
  id: number,
  emailNotificationsPreferences: EmailNotificationsPreferences,
  communicationPreferences: CommunicationPreferences,
  notificationPreferences: NotificationPreferences
}

export interface EmailNotificationsPreferences {
  enabled: boolean
}

export interface CommunicationPreferences {
  enabled: boolean
}

export interface NotificationPreferences {
  enabled: boolean
}

export class PreferencesChangeRequest {
  constructor(
    public emailNotificationsPreferences: EmailNotificationsPreferences,
    public communicationPreferences: CommunicationPreferences,
    public notificationPreferences: NotificationPreferences
  ) {
  }
}

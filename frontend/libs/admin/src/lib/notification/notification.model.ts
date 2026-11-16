export interface NotificationSetting {
  id: number
  name: string
  enabled: boolean
}

export class NotificationSettingPatch {
  constructor(public value: boolean) {}
}

export interface NotificationType {
  id: number
  key: string
  name: string
  description: string
  enabled: boolean
}

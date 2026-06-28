export interface NotificationSetting {
  id: number
  name: string
  enabled: boolean
}

export class NotificationSettingPatch {
  constructor(public value: boolean) {}
}

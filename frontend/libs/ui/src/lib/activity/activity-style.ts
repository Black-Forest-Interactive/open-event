const ACTIVITY_ICONS: Record<string, string> = {
  EVENT_CREATED: 'event',
  EVENT_CHANGED: 'edit_calendar',
  PARTICIPANT_ACCEPTED: 'person_add',
  PARTICIPANT_DECLINED: 'person_remove',
  PARTICIPANT_CHANGED: 'manage_accounts'
}

export function getActivityIcon(type: string): string {
  return ACTIVITY_ICONS[type] ?? 'notifications'
}

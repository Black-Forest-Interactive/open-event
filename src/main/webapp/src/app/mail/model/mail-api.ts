export interface MailJob {
  id: number,
  title: string,
  status: string,
  timestamp: string,
}

export interface MailJobHistoryEntry {
  id: number,
  message: string,
  timestamp: string,
}

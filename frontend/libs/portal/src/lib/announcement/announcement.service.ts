import { Injectable } from '@angular/core'
import { BaseService, Page } from '@open-event/shared'
import { Observable } from 'rxjs'
import { AccountInfo } from '@open-event/core'

export interface Announcement {
  id: number
  subject: string
  content: string
  author: AccountInfo
  timestamp: string
}

export class AnnouncementChangeRequest {
  constructor(public subject: string, public content: string) {}
}

@Injectable({
  providedIn: 'root'
})
export class AnnouncementService extends BaseService {
  constructor() {
    super('app/event')
    this.retryCount = 1
  }

  getAnnouncements(eventId: number, page: number, size: number): Observable<Page<Announcement>> {
    return this.getPaged(`${eventId}/announcement`, page, size)
  }

  createAnnouncement(eventId: number, request: AnnouncementChangeRequest): Observable<Announcement> {
    return this.post(`${eventId}/announcement`, request)
  }
}

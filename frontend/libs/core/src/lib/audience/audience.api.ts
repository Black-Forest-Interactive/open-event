import { Observable } from 'rxjs'
import { Page } from '@open-event/shared'

export interface Audience {
  id: number
  name: string
  iconUrl: string
}

export class AudienceChangeRequest {
  constructor(
    public name: string,
    public iconUrl: string
  ) {}
}

export interface AudienceReadAPI {
  getAudience(id: number): Observable<Audience>

  getAllAudiences(page: number, size: number): Observable<Page<Audience>>
}

import { Injectable } from '@angular/core'
import { BaseService, Page } from '@open-event/shared'
import { Observable } from 'rxjs'
import { EventParticipationSettings, PublicEvent, PublicEventSearchRequest } from './event.api'
import {
  ExternalParticipantAddRequest,
  ExternalParticipantChangeRequest,
  ExternalParticipantChangeResponse,
  ExternalParticipantConfirmRequest,
  ExternalParticipantConfirmResponse
} from '../participant/participant.api'
import { HttpParams } from '@angular/common/http'

@Injectable({
  providedIn: 'root'
})
export class EventService extends BaseService {
  constructor() {
    super('external/event')
    this.retryCount = 0
  }

  getEvent(id: string): Observable<PublicEvent> {
    return this.get(id)
  }

  search(request: PublicEventSearchRequest, page: number, size: number): Observable<Page<PublicEvent>> {
    const params = new HttpParams().set('page', page).set('size', size)
    return this.post('search', request, params)
  }

  getSettings(): Observable<EventParticipationSettings> {
    return this.get('settings')
  }

  requestParticipation(id: string, request: ExternalParticipantAddRequest): Observable<ExternalParticipantChangeResponse> {
    return this.post(id + '/participant', request)
  }

  changeParticipation(id: string, participantId: string, request: ExternalParticipantChangeRequest): Observable<ExternalParticipantChangeResponse> {
    return this.put(id + '/participant/' + participantId, request)
  }

  cancelParticipation(id: string, participantId: string): Observable<ExternalParticipantChangeResponse> {
    return this.delete(id + '/participant/' + participantId)
  }

  confirmParticipation(id: string, participantId: string, request: ExternalParticipantConfirmRequest): Observable<ExternalParticipantConfirmResponse> {
    return this.post(id + '/participant/' + participantId + '/confirm', request)
  }
}

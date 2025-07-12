import {Injectable} from '@angular/core';
import {BaseService} from "@open-event-workspace/shared";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {PublicEvent} from "./event.api";
import {
  ExternalParticipantAddRequest,
  ExternalParticipantChangeRequest,
  ExternalParticipantChangeResponse,
  ExternalParticipantConfirmRequest,
  ExternalParticipantConfirmResponse
} from "../participant/participant.api";

@Injectable({
  providedIn: 'root'
})
export class EventService extends BaseService {

  constructor(http: HttpClient) {
    super(http, 'external/event')
    this.retryCount = 0
  }

  getEvent(id: string): Observable<PublicEvent> {
    return this.get(id)
  }


  requestParticipation(id: string, request: ExternalParticipantAddRequest): Observable<ExternalParticipantChangeResponse> {
    return this.post(id + '/participant', request)
  }

  changeParticipation(id: string, participantId: String, request: ExternalParticipantChangeRequest): Observable<ExternalParticipantChangeResponse> {
    return this.put(id + '/participant/' + participantId, request)
  }

  cancelParticipation(id: string, participantId: string): Observable<ExternalParticipantChangeResponse> {
    return this.delete(id + '/participant/' + participantId)
  }

  confirmParticipation(id: string, participantId: string, request: ExternalParticipantConfirmRequest): Observable<ExternalParticipantConfirmResponse> {
    return this.post(id + '/participant/' + participantId + '/confirm', request)
  }


}

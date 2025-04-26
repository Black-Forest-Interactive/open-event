import {Injectable} from '@angular/core';
import {BaseService} from "@open-event-workspace/shared";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ParticipantAddRequest, ParticipateRequest, ParticipateResponse, RegistrationDetails} from "./registration.api";
import {Participant} from "../participant/participant.api";

/**
 * @deprecated use gateway instead
 */
@Injectable({
  providedIn: 'root'
})
export class RegistrationService extends BaseService {
  constructor(http: HttpClient) {
    super(http, 'registration')
    this.retryCount = 1
  }

  getParticipants(id: number): Observable<Participant[]> {
    return this.get('' + id + '/participant')
  }

  participateSelf(id: number, request: ParticipateRequest): Observable<ParticipateResponse> {
    return this.post('' + id + '/participant', request)
  }

  editSelf(id: number, request: ParticipateRequest): Observable<ParticipateResponse> {
    return this.put('' + id + '/participant', request)
  }

  cancelSelf(id: number): Observable<ParticipateResponse> {
    return this.delete('' + id + '/participant')
  }

  participateAccount(id: number, accountId: number, request: ParticipateRequest): Observable<ParticipateResponse> {
    return this.put('' + id + '/participant/account/' + accountId, request)
  }

  participateManual(id: number, request: ParticipantAddRequest): Observable<ParticipateResponse> {
    return this.post('' + id + '/participant/manual', request)
  }

  changeParticipant(id: number, participantId: number, request: ParticipateRequest): Observable<ParticipateResponse> {
    return this.put(id + '/participant/' + participantId, request)
  }

  removeParticipant(id: number, participantId: number): Observable<ParticipateResponse> {
    return this.delete(id + '/participant/' + participantId)
  }

  getDetails(id: number): Observable<RegistrationDetails> {
    return this.get(id + '/details')
  }

}

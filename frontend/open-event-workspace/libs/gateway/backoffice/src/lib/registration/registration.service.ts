import {Injectable} from "@angular/core";
import {BaseService} from "@open-event-workspace/shared";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ParticipantAddRequest, ParticipateRequest, ParticipateResponse, Registration, RegistrationDetails, RegistrationInfo} from "@open-event-workspace/core";


@Injectable({
  providedIn: 'root'
})
export class RegistrationService extends BaseService {

  constructor(http: HttpClient) {
    super(http, 'backoffice/registration')
    this.retryCount = 0
  }

  getRegistration(id: number): Observable<Registration> {
    return this.get(id + '')
  }

  getRegistrationInfo(id: number): Observable<RegistrationInfo> {
    return this.get(id + '/info')
  }

  getRegistrationDetails(id: number): Observable<RegistrationDetails> {
    return this.get(id + '/details')
  }

  addParticipantAccount(id: number, accountId: number, request: ParticipateRequest): Observable<ParticipateResponse> {
    return this.post('' + id + '/participant/account/' + accountId, request)
  }

  addParticipantManual(id: number, request: ParticipantAddRequest): Observable<ParticipateResponse> {
    return this.post('' + id + '/participant/manual', request)
  }

  changeParticipant(id: number, participantId: number, request: ParticipateRequest): Observable<ParticipateResponse> {
    return this.put(id + '/participant/' + participantId, request)
  }

  removeParticipant(id: number, participantId: number): Observable<ParticipateResponse> {
    return this.delete(id + '/participant/' + participantId)
  }

}

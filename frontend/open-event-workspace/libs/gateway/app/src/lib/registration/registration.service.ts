import {Injectable} from "@angular/core";
import {BaseService} from "@open-event-workspace/shared";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Participant, ParticipantAddRequest, ParticipateRequest, ParticipateResponse, RegistrationDetails} from "@open-event-workspace/core";

@Injectable({
  providedIn: 'root'
})
export class RegistrationService extends BaseService {

  constructor(http: HttpClient) {
    super(http, 'app/registration')
    this.retryCount = 1
  }

  getParticipants(id: number): Observable<Participant[]> {
    return this.get('' + id + '/participant')
  }

  addParticipant(id: number, request: ParticipateRequest): Observable<ParticipateResponse> {
    return this.post('' + id + '/participant', request)
  }

  changeParticipant(id: number, request: ParticipateRequest): Observable<ParticipateResponse> {
    return this.put('' + id + '/participant', request)
  }

  removeParticipant(id: number): Observable<ParticipateResponse> {
    return this.delete('' + id + '/participant')
  }

  moderationParticipateManual(id: number, request: ParticipantAddRequest): Observable<ParticipateResponse> {
    return this.post('' + id + '/participant/manual', request)
  }

  moderationChangeParticipant(id: number, participantId: number, request: ParticipateRequest): Observable<ParticipateResponse> {
    return this.put(id + '/participant/' + participantId, request)
  }

  moderationRemoveParticipant(id: number, participantId: number): Observable<ParticipateResponse> {
    return this.delete(id + '/participant/' + participantId)
  }

  getDetails(id: number): Observable<RegistrationDetails> {
    return this.get(id + '/details')
  }
}

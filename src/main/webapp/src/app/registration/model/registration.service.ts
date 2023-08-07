import {Injectable} from '@angular/core';
import {BaseService} from "../../shared/model/base-service";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Participant} from "../../participant/model/participant-api";
import {ParticipateRequest, ParticipateResponse} from "./registration-api";

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

  participate(id: number, request: ParticipateRequest): Observable<ParticipateResponse> {
    return this.put('' + id + '/participant', request)
  }


}

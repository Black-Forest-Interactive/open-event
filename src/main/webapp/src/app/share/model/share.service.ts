import {Injectable} from '@angular/core';
import {BaseService} from "../../shared/model/base-service";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Share, ShareChangeRequest, SharedParticipateResponse, ShareInfo} from "./share-api";
import {PatchRequest} from "../../event/model/event-api";
import {ParticipantAddRequest} from "../../registration/model/registration-api";

@Injectable({
  providedIn: 'root'
})
export class ShareService extends BaseService {
  constructor(http: HttpClient) {
    super(http, 'share')
    this.retryCount = 0
  }

  findByEvent(eventId: number): Observable<Share> {
    return this.get('find/by/event/' + eventId)
  }

  createShare(request: ShareChangeRequest): Observable<Share> {
    return this.post('', request)
  }

  updateShare(id: string, request: ShareChangeRequest): Observable<Share> {
    return this.put('' + id, request)
  }

  deleteShare(id: string): Observable<Share> {
    return this.delete(id)
  }

  publish(id: string): Observable<Share> {
    return this.put('' + id + '/published', new PatchRequest(true))
  }

  getInfo(id: string): Observable<ShareInfo> {
    return this.get('' + id + '/info')
  }

  addParticipant(id: string, request: ParticipantAddRequest): Observable<SharedParticipateResponse> {
    return this.post(id + '/participant/manual', request)
  }
}

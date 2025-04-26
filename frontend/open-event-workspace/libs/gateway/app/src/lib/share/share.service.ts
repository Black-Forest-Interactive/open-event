import {Injectable} from "@angular/core";
import {BaseService, PatchRequest} from "@open-event-workspace/shared";
import {HttpClient} from "@angular/common/http";
import {Event, Share, ShareChangeRequest} from "@open-event-workspace/core";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ShareService extends BaseService {

  constructor(http: HttpClient) {
    super(http, 'app/share')
    this.retryCount = 1
  }

  getShareForEvent(event: Event): Observable<Share> {
    return this.get('by/event/' + event.id)
  }

  createShare(request: ShareChangeRequest): Observable<Share> {
    return this.post('', request)
  }

  updateShare(id: string, request: ShareChangeRequest): Observable<Share> {
    return this.put('' + id, request)
  }

  publish(id: string): Observable<Share> {
    return this.put('' + id + '/published', new PatchRequest(true))
  }

}

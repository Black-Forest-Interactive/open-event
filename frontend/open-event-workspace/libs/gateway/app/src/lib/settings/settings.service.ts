import {Injectable} from "@angular/core";
import {BaseService} from "@open-event-workspace/shared";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {TextResponse} from "@open-event-workspace/core";

@Injectable({
  providedIn: 'root'
})
export class SettingsService extends BaseService {

  constructor(http: HttpClient) {
    super(http, 'app/settings')
    this.retryCount = 1
  }

  getTitle(): Observable<TextResponse> {
    return this.get('title')
  }
}

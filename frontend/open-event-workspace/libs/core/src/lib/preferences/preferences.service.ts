import {Injectable} from '@angular/core';
import {BaseService} from "@open-event-workspace/shared";
import {HttpClient} from "@angular/common/http";

/**
 * @deprecated use gateway instead
 */
@Injectable({
  providedIn: 'root'
})
export class PreferencesService extends BaseService {
  constructor(http: HttpClient) {
    super(http, 'preferences')
    this.retryCount = 0
  }
}

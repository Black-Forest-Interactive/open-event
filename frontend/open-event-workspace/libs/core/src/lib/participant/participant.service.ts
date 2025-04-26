import {Injectable} from '@angular/core';
import {BaseService} from "@open-event-workspace/shared";
import {HttpClient} from "@angular/common/http";

/**
 * @deprecated use gateway instead
 */
@Injectable({
  providedIn: 'root'
})
export class ParticipantService extends BaseService {
  constructor(http: HttpClient) {
    super(http, 'participant')
    this.retryCount = 1
  }
}

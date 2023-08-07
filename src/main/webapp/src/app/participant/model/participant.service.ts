import {Injectable} from '@angular/core';
import {BaseService} from "../../shared/model/base-service";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ParticipantService extends BaseService {
  constructor(http: HttpClient) {
    super(http, 'participant')
    this.retryCount = 1
  }
}

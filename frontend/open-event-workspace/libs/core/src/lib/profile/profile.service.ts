import {Injectable} from '@angular/core';
import {BaseService} from "@open-event-workspace/shared";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Profile, ProfileChangeRequest} from "./profile.api";

/**
 * @deprecated use gateway instead
 */
@Injectable({
  providedIn: 'root'
})
export class ProfileService extends BaseService {
  constructor(http: HttpClient) {
    super(http, 'profile')
    this.retryCount = 0
  }

  updateProfile(id: number, request: ProfileChangeRequest): Observable<Profile> {
    return this.put('' + id, request)
  }
}

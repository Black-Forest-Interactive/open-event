import {Injectable} from '@angular/core';
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class EventNavigationService {


  constructor() {
  }

  static getEventCreateUrl() {
    return "/event/create"
  }

  static getEventShowUrl() {
    return "/event"
  }

  static navigateToEventShow(router: Router) {
    router.navigate([EventNavigationService.getEventShowUrl()]).then()
  }

  static navigateToEventCreate(router: Router, sourceId: string | undefined = undefined) {
    router.navigate([EventNavigationService.getEventCreateUrl(), {source: sourceId}]).then()
  }


  static navigateToEventDetails(router: Router, eventId: number) {
    router.navigate(["/event/details/" + eventId]).then()
  }

  static navigateToEventEdit(router: Router, eventId: number) {
    router.navigate(["/event/edit/" + eventId]).then()
  }

  static navigateToEventCopy(router: Router, eventId: number) {
    router.navigate(["/event/copy/" + eventId]).then()
  }

  static navigateToEventAdministration(router: Router, eventId: number) {
    router.navigate(["/event/admin/" + eventId]).then()
  }
}

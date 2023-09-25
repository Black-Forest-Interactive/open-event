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
    router.navigate([EventNavigationService.getEventShowUrl()]);
  }

  static navigateToEventCreate(router: Router, sourceId: string | undefined = undefined) {
    router.navigate([EventNavigationService.getEventCreateUrl(), {source: sourceId}])
  }


  static navigateToEventDetails(router: Router, eventId: number) {
    router.navigate(["/event/details/" + eventId])
  }

  static navigateToEventEdit(router: Router, eventId: number) {
    router.navigate(["/event/edit/" + eventId])
  }

  static navigateToEventCopy(router: Router, eventId: number) {
    router.navigate(["/event/copy/" + eventId])
  }
}

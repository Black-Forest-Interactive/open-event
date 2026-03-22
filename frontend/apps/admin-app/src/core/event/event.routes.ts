import {Routes} from "@angular/router";

export const routes: Routes = [
  {path: '', loadComponent: () => import('./event.component').then(m => m.EventComponent)},
  {path: 'details/:id', loadComponent: () => import('./event-details/event-details.component').then(m => m.EventDetailsComponent)},
];

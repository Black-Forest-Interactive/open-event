import {Routes} from "@angular/router";

export const routes: Routes = [
  {path: '', loadComponent: () => import('./activity.component').then(m => m.ActivityComponent)},
  {path: 'details/:id', loadComponent: () => import('./activity-details/activity-details.component').then(m => m.ActivityDetailsComponent)},
];

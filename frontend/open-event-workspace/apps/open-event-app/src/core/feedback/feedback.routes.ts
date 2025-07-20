import {Routes} from "@angular/router";

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./feedback.component').then(m => m.FeedbackComponent),
  }
];

import { Routes } from '@angular/router'

export const routes: Routes = [
  { path: '', loadComponent: () => import('./feedback.component').then((m) => m.FeedbackComponent) },
  { path: 'details/:id', loadComponent: () => import('./feedback-details/feedback-details.component').then((m) => m.FeedbackDetailsComponent) }
]

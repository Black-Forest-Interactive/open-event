import { Routes } from '@angular/router'

export const routes: Routes = [
  { path: '', loadComponent: () => import('./newsletter.component').then((m) => m.NewsletterComponent) }
]

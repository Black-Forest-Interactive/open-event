import { Routes } from '@angular/router'

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./activity.component').then((m) => m.ActivityComponent)
  }
]

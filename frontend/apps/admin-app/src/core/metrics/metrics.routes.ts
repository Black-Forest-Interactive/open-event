import { Routes } from '@angular/router'

export const routes: Routes = [{ path: '', loadComponent: () => import('./metrics.component').then((m) => m.MetricsComponent) }]

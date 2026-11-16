import { Routes } from '@angular/router'

export const routes: Routes = [{ path: '', loadComponent: () => import('./notification.component').then((m) => m.NotificationComponent) }]

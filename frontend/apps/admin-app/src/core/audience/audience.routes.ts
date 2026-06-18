import { Routes } from '@angular/router'

export const routes: Routes = [{ path: '', loadComponent: () => import('./audience.component').then((m) => m.AudienceComponent) }]

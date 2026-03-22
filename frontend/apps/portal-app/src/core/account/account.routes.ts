import {Routes} from "@angular/router";

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./account.component').then(m => m.AccountComponent)
  },
];

import {Routes} from "@angular/router";

export const routes: Routes = [
  {path: '', loadComponent: () => import('./account.component').then(m => m.AccountComponent)},
  {path: 'details/:id', loadComponent: () => import('./account-details/account-details.component').then(m => m.AccountDetailsComponent)},
];

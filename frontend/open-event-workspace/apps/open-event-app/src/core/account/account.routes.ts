import {Routes} from "@angular/router";

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./account-profile/account-profile.component').then(m => m.AccountProfileComponent)
  },
];

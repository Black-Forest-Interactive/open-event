import {Routes} from "@angular/router";

export const routes: Routes = [
  {path: '', loadComponent: () => import('./mail.component').then(m => m.MailComponent)},
];

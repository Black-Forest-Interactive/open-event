import {Routes} from "@angular/router";

export const routes: Routes = [
  {path: '', loadComponent: () => import('./mail.component').then(m => m.MailComponent)},
  {path: 'history/:id', loadComponent: () => import('./mail-history/mail-history.component').then(m => m.MailHistoryComponent)},
];

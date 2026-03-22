import {Routes} from "@angular/router";

export const routes: Routes = [
  {path: '', loadComponent: () => import('./history.component').then(m => m.HistoryComponent)},
];

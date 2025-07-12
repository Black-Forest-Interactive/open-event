import {Routes} from "@angular/router";

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./imprint.component').then(m => m.ImprintComponent),
  }
];

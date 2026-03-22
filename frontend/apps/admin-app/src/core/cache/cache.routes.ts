import {Routes} from "@angular/router";

export const routes: Routes = [
  {path: '', loadComponent: () => import('./cache.component').then(m => m.CacheComponent)},
];

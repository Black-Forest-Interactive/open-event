import {Routes} from "@angular/router";

export const routes: Routes = [
  {path: '', loadComponent: () => import('./search.component').then(m => m.SearchComponent)},
];

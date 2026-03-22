import {Routes} from "@angular/router";

export const routes: Routes = [
  {path: '', loadComponent: () => import('./category.component').then(m => m.CategoryComponent)},
];

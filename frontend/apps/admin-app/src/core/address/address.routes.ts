import {Routes} from "@angular/router";

export const routes: Routes = [
  {path: '', loadComponent: () => import('./address.component').then(m => m.AddressComponent)},
];

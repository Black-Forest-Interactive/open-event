import {Routes} from "@angular/router";

export const routes: Routes = [
  {
    path: 'info/:id',
    loadComponent: () => import('./share-info/share-info.component').then(m => m.ShareInfoComponent),
  },
];

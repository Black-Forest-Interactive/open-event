import {Routes} from "@angular/router";

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./address-board/address-board.component').then(m => m.AddressBoardComponent)
  },
];

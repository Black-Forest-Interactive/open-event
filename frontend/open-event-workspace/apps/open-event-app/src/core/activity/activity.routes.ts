import {Routes} from "@angular/router";

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./activity-board/activity-board.component').then(m => m.ActivityBoardComponent)
  },
];

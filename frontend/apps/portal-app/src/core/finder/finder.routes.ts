import { Routes } from '@angular/router'

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./finder-board/finder-board.component').then((m) => m.FinderBoardComponent)
  },
  {
    path: ':id',
    loadComponent: () => import('./finder-session/finder-session.component').then((m) => m.FinderSessionComponent)
  }
]

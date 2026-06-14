import { Routes } from '@angular/router'

export const routes: Routes = [
  {
    path: ':id',
    children: [
      {
        path: '',
        loadComponent: () => import('./event/event.component').then((m) => m.EventComponent)
      },
      {
        path: 'confirm',
        loadComponent: () => import('./event-confirm/event-confirm.component').then((m) => m.EventConfirmComponent)
      },
      {
        path: 'search',
        loadComponent: () => import('./event-board/event-board.component').then((m) => m.EventBoardComponent)
      }
    ]
  }
]

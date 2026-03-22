import {Routes} from "@angular/router";

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./event-board/event-board.component').then(m => m.EventBoardComponent),
  },
  {
    path: 'create',
    loadComponent: () => import('./event-create/event-create.component').then(m => m.EventCreateComponent),
  },
  {
    path: 'details/:id',
    loadComponent: () => import('./event-details/event-details.component').then(m => m.EventDetailsComponent),
  },
  {
    path: 'edit/:id',
    loadComponent: () => import('./event-edit/event-edit.component').then(m => m.EventEditComponent),
  },
  {
    path: 'copy/:id',
    loadComponent: () => import('./event-copy/event-copy.component').then(m => m.EventCopyComponent),
  },
  {
    path: 'admin/:id',
    loadComponent: () => import('./event-admin/event-admin.component').then(m => m.EventAdminComponent),
  },
];

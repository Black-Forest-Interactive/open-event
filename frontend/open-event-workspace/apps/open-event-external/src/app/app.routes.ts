import {Route} from '@angular/router';

export const appRoutes: Route[] = [
  {
    path: 'event',
    loadChildren: () => import('../core/event/event.routes').then(m => m.routes),
  },
];

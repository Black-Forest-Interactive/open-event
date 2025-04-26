import {Route} from '@angular/router';
import {canActivateAuthRole, ForbiddenComponent, PageNotFoundComponent} from "@open-event-workspace/shared";
import {Roles} from "../shared/roles";

export const appRoutes: Route[] = [
  {path: '', pathMatch: 'full', redirectTo: 'event'},
  {
    path: 'event',
    loadChildren: () => import('../core/event/event.routes').then(m => m.routes),
    canActivate: [canActivateAuthRole],
  },
  {
    path: 'account',
    loadChildren: () => import('../core/account/account.routes').then(m => m.routes),
    canActivate: [canActivateAuthRole],
    data: {roles: [Roles.ACCOUNT_READ]}
  },
  {
    path: 'address',
    loadChildren: () => import('../core/address/address.routes').then(m => m.routes),
    canActivate: [canActivateAuthRole],
    data: {roles: [Roles.ADDRESS_READ]}
  },
  {
    path: 'activity',
    loadChildren: () => import('../core/activity/activity.routes').then(m => m.routes),
    canActivate: [canActivateAuthRole],
    data: {roles: [Roles.ACTIVITY_READ]}
  },
  {
    path: 'share',
    loadChildren: () => import('../core/share/share.routes').then(m => m.routes),
    data: {public: true}
  },
  {path: 'forbidden', component: ForbiddenComponent},
  {path: '**', component: PageNotFoundComponent}
]

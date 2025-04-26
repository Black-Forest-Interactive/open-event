import {Route} from '@angular/router';
import {canActivateAuthRole, ForbiddenComponent, PageNotFoundComponent} from "@open-event-workspace/shared";
import {Roles} from "../shared/roles";

export const appRoutes: Route[] = [
  {path: '', pathMatch: 'full', redirectTo: 'event'},
  {
    path: 'event',
    loadChildren: () => import('../core/event/event.routes').then(m => m.routes),
    canActivate: [canActivateAuthRole],
    data: {roles: [Roles.EVENT_ADMIN]}
  },
  {
    path: 'account',
    loadChildren: () => import('../core/account/account.routes').then(m => m.routes),
    canActivate: [canActivateAuthRole],
    data: {roles: [Roles.ACCOUNT_ADMIN]}
  },
  {
    path: 'activity',
    loadChildren: () => import('../core/activity/activity.routes').then(m => m.routes),
    canActivate: [canActivateAuthRole],
    data: {roles: [Roles.ACTIVITY_ADMIN]}
  },
  {
    path: 'address',
    loadChildren: () => import('../core/address/address.routes').then(m => m.routes),
    canActivate: [canActivateAuthRole],
    data: {roles: [Roles.ADDRESS_ADMIN]}
  },
  {
    path: 'category',
    loadChildren: () => import('../core/category/category.routes').then(m => m.routes),
    canActivate: [canActivateAuthRole],
    data: {roles: [Roles.CATEGORY_ADMIN]}
  },
  {
    path: 'settings',
    loadChildren: () => import('../core/settings/settings.routes').then(m => m.routes),
    canActivate: [canActivateAuthRole],
    data: {roles: [Roles.SETTINGS_ADMIN]}
  },
  {
    path: 'cache',
    loadChildren: () => import('../core/cache/cache.routes').then(m => m.routes),
    canActivate: [canActivateAuthRole],
    data: {roles: [Roles.CACHE_ADMIN]}
  },
  {
    path: 'mail',
    loadChildren: () => import('../core/mail/mail.routes').then(m => m.routes),
    canActivate: [canActivateAuthRole],
    data: {roles: [Roles.MAIL_ADMIN]}
  },
  {
    path: 'history',
    loadChildren: () => import('../core/history/history.routes').then(m => m.routes),
    canActivate: [canActivateAuthRole],
    data: {roles: [Roles.HISTORY_ADMIN]}
  },
  {
    path: 'search',
    loadChildren: () => import('../core/search/search.routes').then(m => m.routes),
    canActivate: [canActivateAuthRole],
    data: {roles: [Roles.SEARCH_ADMIN]}
  },
  {
    path: 'issue',
    loadChildren: () => import('../core/issue/issue.routes').then(m => m.routes),
    canActivate: [canActivateAuthRole],
    data: {roles: [Roles.ISSUE_ADMIN]}
  },
  {path: 'forbidden', component: ForbiddenComponent},
  {path: '**', component: PageNotFoundComponent}
];

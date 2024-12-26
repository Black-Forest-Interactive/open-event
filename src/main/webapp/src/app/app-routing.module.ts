import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthGuard} from "./auth/auth.guard";
import {AuthService} from "./auth/auth.service";
import {PageNotFoundComponent} from "./dashboard/page-not-found/page-not-found.component";

const routes: Routes = [
  {path: '', pathMatch: 'full', redirectTo: 'event'},
  {
    path: 'event',
    loadChildren: () => import('./event/event.module').then(m => m.EventModule),
    canActivate: [AuthGuard],
  },
  {
    path: 'category',
    loadChildren: () => import('./category/category.module').then(m => m.CategoryModule),
    canActivate: [AuthGuard],
    data: {roles: [AuthService.CATEGORY_READ, AuthService.CATEGORY_WRITE]}
  },
  {
    path: 'settings',
    loadChildren: () => import('./settings/settings.module').then(m => m.SettingsModule),
    canActivate: [AuthGuard],
    data: {roles: [AuthService.SETTINGS_READ, AuthService.SETTINGS_WRITE]}
  },
  {
    path: 'cache',
    loadChildren: () => import('./cache/cache.module').then(m => m.CacheModule),
    canActivate: [AuthGuard],
    data: {roles: [AuthService.CACHE_READ, AuthService.CACHE_WRITE]}
  },
  {
    path: 'mail',
    loadChildren: () => import('./mail/mail.module').then(m => m.MailModule),
    canActivate: [AuthGuard],
    data: {roles: [AuthService.MAIL_READ, AuthService.MAIL_WRITE]}
  },
  {
    path: 'backoffice',
    loadChildren: () => import('./backoffice/backoffice.module').then(m => m.BackofficeModule),
    canActivate: [AuthGuard],
    data: {roles: [AuthService.BACKOFFICE_ACCESS]}
  },
  {
    path: 'history',
    loadChildren: () => import('./history/history.module').then(m => m.HistoryModule),
    canActivate: [AuthGuard],
    data: {roles: [AuthService.HISTORY_ADMIN]}
  },
  {
    path: 'account',
    loadChildren: () => import('./account/account.module').then(m => m.AccountModule),
    canActivate: [AuthGuard],
    data: {roles: [AuthService.ACCOUNT_READ]}
  },
  {
    path: 'share',
    loadChildren: () => import('./share/share.module').then(m => m.ShareModule),
    data: {public: true}
  },
  {
    path: '**', component: PageNotFoundComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

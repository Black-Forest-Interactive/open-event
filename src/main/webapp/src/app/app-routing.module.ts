import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthGuard} from "./auth/auth.guard";
import {AuthService} from "./auth/auth.service";
import {PageNotFoundComponent} from "./dashboard/page-not-found/page-not-found.component";

const routes: Routes = [
  {path: '', pathMatch: 'full', redirectTo: 'event'},
  {path: 'event', loadChildren: () => import('./event/event.module').then(m => m.EventModule)},
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
    path: '**', component: PageNotFoundComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

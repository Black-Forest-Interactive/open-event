import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthGuard} from "./auth/auth.guard";
import {AuthService} from "./auth/auth.service";

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
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

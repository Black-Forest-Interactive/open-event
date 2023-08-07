import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

const routes: Routes = [
  {path: '', pathMatch: 'full', redirectTo: 'event'},
  {path: 'event', loadChildren: () => import('./event/event.module').then(m => m.EventModule)},
  {path: 'category', loadChildren: () => import('./category/category.module').then(m => m.CategoryModule)},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

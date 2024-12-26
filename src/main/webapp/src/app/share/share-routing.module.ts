import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ShareInfoComponent} from "./share-info/share-info.component";

const routes: Routes = [
  {path: 'info/:id', component: ShareInfoComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ShareRoutingModule {
}

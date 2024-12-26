import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {BackofficeBoardComponent} from "./backoffice-board/backoffice-board.component";
import {BackofficeAccountComponent} from "./backoffice-account/backoffice-account.component";
import {BackofficeAdminComponent} from "./backoffice-admin/backoffice-admin.component";
import {BackofficeEventComponent} from "./backoffice-event/backoffice-event.component";

const routes: Routes = [
  {
    path: '', component: BackofficeBoardComponent, children: [
      {path: '', redirectTo: 'account', pathMatch: 'full'},
      {path: 'account', component: BackofficeAccountComponent},
      {path: 'event', component: BackofficeEventComponent},
      {path: 'admin', component: BackofficeAdminComponent},
    ]
  },

  // {path: 'event', component: EventCreateComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BackofficeRoutingModule {
}

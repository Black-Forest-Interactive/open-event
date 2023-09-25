import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {BackofficeBoardComponent} from "./backoffice-board/backoffice-board.component";
import {EventCreateComponent} from "./event-create/event-create.component";

const routes: Routes = [
  {path: '', component: BackofficeBoardComponent},
  {path: 'event', component: EventCreateComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BackofficeRoutingModule {
}

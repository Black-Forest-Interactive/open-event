import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MailBoardComponent} from "./mail-board/mail-board.component";
import {MailHistoryComponent} from "./mail-history/mail-history.component";

const routes: Routes = [
  {path: '', component: MailBoardComponent},
  {path: 'history/:id', component: MailHistoryComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MailRoutingModule {
}

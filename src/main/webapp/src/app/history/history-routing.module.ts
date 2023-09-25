import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HistoryBoardComponent} from "./history-board/history-board.component";

const routes: Routes = [
  {path: '', component: HistoryBoardComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HistoryRoutingModule {
}

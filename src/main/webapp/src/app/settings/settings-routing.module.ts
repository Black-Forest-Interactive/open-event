import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SettingsBoardComponent} from "./settings-board/settings-board.component";

const routes: Routes = [
  {path: '', component: SettingsBoardComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SettingsRoutingModule {
}

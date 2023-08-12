import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SettingsBoardComponent} from "./settings-board/settings-board.component";
import {SettingsChangeComponent} from "./settings-change/settings-change.component";

const routes: Routes = [
  {path: '', component: SettingsBoardComponent},
  {path: 'create', component: SettingsChangeComponent},
  {path: 'edit/:id', component: SettingsChangeComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SettingsRoutingModule {
}

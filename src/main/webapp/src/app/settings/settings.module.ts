import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SettingsRoutingModule} from './settings-routing.module';
import {SettingsBoardComponent} from './settings-board/settings-board.component';
import {MaterialModule} from "../material/material.module";
import {TranslateModule} from "@ngx-translate/core";
import {ReactiveFormsModule} from "@angular/forms";
import {SettingsChangeDialogComponent} from './settings-change-dialog/settings-change-dialog.component';


@NgModule({
  declarations: [
    SettingsBoardComponent,
    SettingsChangeDialogComponent
  ],
  imports: [
    CommonModule,
    SettingsRoutingModule,
    MaterialModule,
    TranslateModule,
    ReactiveFormsModule,
  ]
})
export class SettingsModule {
}

import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {SettingsRoutingModule} from './settings-routing.module';
import {SettingsBoardComponent} from './settings-board/settings-board.component';
import {SettingsChangeComponent} from './settings-change/settings-change.component';
import {MaterialModule} from "../material/material.module";
import {TranslateModule} from "@ngx-translate/core";
import {HotToastModule} from "@ngneat/hot-toast";
import {ReactiveFormsModule} from "@angular/forms";


@NgModule({
  declarations: [
    SettingsBoardComponent,
    SettingsChangeComponent
  ],
  imports: [
    CommonModule,
    SettingsRoutingModule,
    MaterialModule,
    TranslateModule,
    HotToastModule,
    ReactiveFormsModule,
  ]
})
export class SettingsModule { }

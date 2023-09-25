import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {HistoryRoutingModule} from './history-routing.module';
import {HistoryBoardComponent} from './history-board/history-board.component';
import {TranslateModule} from "@ngx-translate/core";
import {AccountModule} from "../account/account.module";
import {MaterialModule} from "../material/material.module";
import {EventModule} from "../event/event.module";
import {ReactiveFormsModule} from "@angular/forms";


@NgModule({
  declarations: [
    HistoryBoardComponent
  ],
  imports: [
    CommonModule,
    HistoryRoutingModule,
    TranslateModule,
    AccountModule,
    MaterialModule,
    EventModule,
    ReactiveFormsModule
  ]
})
export class HistoryModule {
}

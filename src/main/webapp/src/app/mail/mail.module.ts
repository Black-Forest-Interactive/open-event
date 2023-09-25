import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {MailRoutingModule} from './mail-routing.module';
import {MailBoardComponent} from './mail-board/mail-board.component';
import {MailHistoryComponent} from './mail-history/mail-history.component';
import {MaterialModule} from "../material/material.module";
import {TranslateModule} from "@ngx-translate/core";
import {ReactiveFormsModule} from "@angular/forms";
import {HotToastModule} from "@ngneat/hot-toast";
import {NgxEchartsModule} from "ngx-echarts";


@NgModule({
  declarations: [
    MailBoardComponent,
    MailHistoryComponent
  ],
  imports: [
    CommonModule,
    MailRoutingModule,
    MaterialModule,
    TranslateModule,
    ReactiveFormsModule,
    HotToastModule,
    NgxEchartsModule.forChild(),
  ]
})
export class MailModule { }

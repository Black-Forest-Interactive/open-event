import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {BackofficeBoardComponent} from './backoffice-board/backoffice-board.component';
import {MaterialModule} from "../material/material.module";
import {TranslateModule} from "@ngx-translate/core";
import {ReactiveFormsModule} from "@angular/forms";
import {HotToastModule} from "@ngneat/hot-toast";
import {NgxEchartsModule} from "ngx-echarts";
import {BackofficeRoutingModule} from "./backoffice-routing.module";
import {RegistrationModule} from "../registration/registration.module";
import {BoardCardStatsComponent} from './board-card-stats/board-card-stats.component';
import {BoardCardExportComponent} from './board-card-export/board-card-export.component';
import {BoardCardSolrComponent} from './board-card-solr/board-card-solr.component';
import {BoardCardAccountComponent} from './board-card-account/board-card-account.component';
import {BoardCardEventComponent} from './board-card-event/board-card-event.component';
import {EventCreateComponent} from './event-create/event-create.component';
import {EventModule} from "../event/event.module";
import {AccountModule} from "../account/account.module";


@NgModule({
  declarations: [
    BackofficeBoardComponent,
    BoardCardStatsComponent,
    BoardCardExportComponent,
    BoardCardSolrComponent,
    BoardCardAccountComponent,
    BoardCardEventComponent,
    EventCreateComponent
  ],
  imports: [
    CommonModule,
    BackofficeRoutingModule,
    MaterialModule,
    TranslateModule,
    ReactiveFormsModule,
    HotToastModule,
    NgxEchartsModule.forChild(),
    RegistrationModule,
    EventModule,
    AccountModule,
  ]
})
export class BackofficeModule {
}

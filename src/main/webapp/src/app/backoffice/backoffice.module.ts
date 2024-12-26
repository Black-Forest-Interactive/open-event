import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {BackofficeBoardComponent} from './backoffice-board/backoffice-board.component';
import {MaterialModule} from "../material/material.module";
import {TranslateModule} from "@ngx-translate/core";
import {ReactiveFormsModule} from "@angular/forms";
import {NgxEchartsModule} from "ngx-echarts";
import {BackofficeRoutingModule} from "./backoffice-routing.module";
import {RegistrationModule} from "../registration/registration.module";
import {BoardCardStatsComponent} from './board-card-stats/board-card-stats.component';
import {BoardCardExportComponent} from './board-card-export/board-card-export.component';
import {BoardCardAccountComponent} from './board-card-account/board-card-account.component';
import {BoardCardEventComponent} from './board-card-event/board-card-event.component';
import {EventCreateComponent} from './event-create/event-create.component';
import {EventModule} from "../event/event.module";
import {AccountModule} from "../account/account.module";
import {BackofficeEventComponent} from './backoffice-event/backoffice-event.component';
import {BackofficeAccountComponent} from './backoffice-account/backoffice-account.component';
import {BackofficeAdminComponent} from './backoffice-admin/backoffice-admin.component';
import {BoardCardSearchComponent} from './board-card-search/board-card-search.component';


@NgModule({
  declarations: [
    BackofficeBoardComponent,
    BoardCardStatsComponent,
    BoardCardExportComponent,
    BoardCardAccountComponent,
    BoardCardEventComponent,
    EventCreateComponent,
    BackofficeEventComponent,
    BackofficeAccountComponent,
    BackofficeAdminComponent,
    BoardCardSearchComponent,
  ],
  imports: [
    CommonModule,
    BackofficeRoutingModule,
    MaterialModule,
    TranslateModule,
    ReactiveFormsModule,
    NgxEchartsModule.forChild(),
    RegistrationModule,
    EventModule,
    AccountModule,
  ]
})
export class BackofficeModule {
}

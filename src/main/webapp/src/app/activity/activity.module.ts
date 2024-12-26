import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {ActivityRoutingModule} from './activity-routing.module';
import {ActivityButtonComponent} from './activity-button/activity-button.component';
import {MaterialModule} from "../material/material.module";
import {TranslateModule} from "@ngx-translate/core";
import {ActivityTableComponent} from './activity-table/activity-table.component';
import {ActivityReadComponent} from './activity-read/activity-read.component';


@NgModule({
  declarations: [
    ActivityButtonComponent,
    ActivityTableComponent,
    ActivityReadComponent
  ],
    exports: [
        ActivityButtonComponent,
        ActivityTableComponent
    ],
  imports: [
    CommonModule,
    ActivityRoutingModule,
    MaterialModule,
    TranslateModule
  ]
})
export class ActivityModule {
}

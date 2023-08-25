import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CacheRoutingModule} from "./cache-routing.module";
import {MaterialModule} from "../material/material.module";
import {TranslateModule} from "@ngx-translate/core";
import {ReactiveFormsModule} from "@angular/forms";
import {HotToastModule} from "@ngneat/hot-toast";
import { CacheBoardComponent } from './cache-board/cache-board.component';
import {NgxEchartsModule} from "ngx-echarts";


@NgModule({
  declarations: [
    CacheBoardComponent
  ],
  imports: [
    CommonModule,
    CacheRoutingModule,
    MaterialModule,
    TranslateModule,
    ReactiveFormsModule,
    HotToastModule,
    NgxEchartsModule.forChild(),
  ]
})
export class CacheModule {
}

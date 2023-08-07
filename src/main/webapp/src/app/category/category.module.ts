import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CategoryBoardComponent} from './category-board/category-board.component';
import {CategoryRoutingModule} from "./category-routing.module";
import {TranslateModule} from "@ngx-translate/core";
import {MaterialModule} from "../material/material.module";
import {ReactiveFormsModule} from "@angular/forms";
import {HotToastModule} from "@ngneat/hot-toast";
import {CategoryChangeComponent} from './category-change/category-change.component';


@NgModule({
  declarations: [
    CategoryBoardComponent,
    CategoryChangeComponent
  ],
  imports: [
    CommonModule,
    CategoryRoutingModule,
    MaterialModule,
    TranslateModule,
    ReactiveFormsModule,
    HotToastModule,
  ]
})
export class CategoryModule {
}

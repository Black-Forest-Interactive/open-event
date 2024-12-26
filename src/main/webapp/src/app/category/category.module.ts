import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CategoryBoardComponent} from './category-board/category-board.component';
import {CategoryRoutingModule} from "./category-routing.module";
import {TranslateModule} from "@ngx-translate/core";
import {MaterialModule} from "../material/material.module";
import {ReactiveFormsModule} from "@angular/forms";
import {AccountModule} from "../account/account.module";
import {RegistrationModule} from "../registration/registration.module";
import {CategoryDeleteDialogComponent} from './category-delete-dialog/category-delete-dialog.component';
import {CategoryChangeDialogComponent} from './category-change-dialog/category-change-dialog.component';


@NgModule({
  declarations: [
    CategoryBoardComponent,
    CategoryDeleteDialogComponent,
    CategoryChangeDialogComponent
  ],
  imports: [
    CommonModule,
    CategoryRoutingModule,
    MaterialModule,
    TranslateModule,
    ReactiveFormsModule,
    AccountModule,
    RegistrationModule,
  ]
})
export class CategoryModule {
}

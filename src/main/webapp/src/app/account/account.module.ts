import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AccountComponent} from './account/account.component';
import {AccountDisplayNamePipe} from './account-display-name.pipe';
import {GravatarModule} from "ngx-gravatar";
import {CreateAccountDialogComponent} from './create-account-dialog/create-account-dialog.component';
import {SearchAccountDialogComponent} from './search-account-dialog/search-account-dialog.component';
import {MaterialModule} from "../material/material.module";
import {TranslateModule} from "@ngx-translate/core";
import {ReactiveFormsModule} from "@angular/forms";
import {HotToastModule} from "@ngneat/hot-toast";
import {SelectAccountComponent} from './select-account/select-account.component';


@NgModule({
  declarations: [
    AccountComponent,
    AccountDisplayNamePipe,
    CreateAccountDialogComponent,
    SearchAccountDialogComponent,
    SelectAccountComponent
  ],
  exports: [
    AccountComponent,
    AccountDisplayNamePipe,
    SelectAccountComponent
  ],
  imports: [
    CommonModule,
    GravatarModule,
    MaterialModule,
    TranslateModule,
    ReactiveFormsModule,
    HotToastModule
  ]
})
export class AccountModule {
}

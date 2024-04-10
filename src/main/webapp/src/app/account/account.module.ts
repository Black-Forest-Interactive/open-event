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
import {SelectAccountComponent} from './select-account/select-account.component';
import {AccountBoardComponent} from './account-board/account-board.component';
import {AccountRoutingModule} from "./account-routing.module";
import {AccountProfileComponent} from './account-profile/account-profile.component';
import {AccountAddressComponent} from './account-address/account-address.component';
import {AccountPreferencesComponent} from './account-preferences/account-preferences.component';


@NgModule({
  declarations: [
    AccountComponent,
    AccountDisplayNamePipe,
    CreateAccountDialogComponent,
    SearchAccountDialogComponent,
    SelectAccountComponent,
    AccountBoardComponent,
    AccountProfileComponent,
    AccountAddressComponent,
    AccountPreferencesComponent
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
    AccountRoutingModule
  ]
})
export class AccountModule {
}

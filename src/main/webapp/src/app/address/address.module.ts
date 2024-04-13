import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AddressBoardComponent} from './address-board/address-board.component';
import {PreferencesModule} from "../preferences/preferences.module";
import {AddressChangeComponent} from './address-change/address-change.component';
import {AddressChangeDialogComponent} from './address-change-dialog/address-change-dialog.component';
import {MaterialModule} from "../material/material.module";
import {TranslateModule} from "@ngx-translate/core";
import {ReactiveFormsModule} from "@angular/forms";


@NgModule({
  declarations: [
    AddressBoardComponent,
    AddressChangeComponent,
    AddressChangeDialogComponent
  ],
  exports: [
    AddressBoardComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    TranslateModule,
    ReactiveFormsModule,
    PreferencesModule,
  ]
})
export class AddressModule {
}

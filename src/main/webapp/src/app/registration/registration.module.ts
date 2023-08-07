import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RegistrationDetailsComponent} from './registration-details/registration-details.component';
import {MaterialModule} from "../material/material.module";
import {TranslateModule} from "@ngx-translate/core";
import {HotToastModule} from "@ngneat/hot-toast";
import {
  RegistrationParticipateDialogComponent
} from './registration-participate-dialog/registration-participate-dialog.component';
import {ReactiveFormsModule} from "@angular/forms";
import {AccountModule} from "../account/account.module";


@NgModule({
  declarations: [
    RegistrationDetailsComponent,
    RegistrationParticipateDialogComponent
  ],
  exports: [
    RegistrationDetailsComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    TranslateModule,
    HotToastModule,
    ReactiveFormsModule,
    AccountModule
  ]
})
export class RegistrationModule {
}

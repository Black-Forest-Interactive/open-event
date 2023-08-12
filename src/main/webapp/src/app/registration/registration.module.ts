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
import {RegistrationEditDialogComponent} from './registration-edit-dialog/registration-edit-dialog.component';
import {RegistrationCancelDialogComponent} from './registration-cancel-dialog/registration-cancel-dialog.component';
import {
  RegistrationParticipateAccountDialogComponent
} from './registration-participate-account-dialog/registration-participate-account-dialog.component';
import {
  RegistrationParticipateManualDialogComponent
} from './registration-participate-manual-dialog/registration-participate-manual-dialog.component';
import {RegistrationStatusComponent} from './registration-status/registration-status.component';


@NgModule({
  declarations: [
    RegistrationDetailsComponent,
    RegistrationParticipateDialogComponent,
    RegistrationEditDialogComponent,
    RegistrationCancelDialogComponent,
    RegistrationParticipateAccountDialogComponent,
    RegistrationParticipateManualDialogComponent,
    RegistrationStatusComponent
  ],
    exports: [
        RegistrationDetailsComponent,
        RegistrationStatusComponent
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

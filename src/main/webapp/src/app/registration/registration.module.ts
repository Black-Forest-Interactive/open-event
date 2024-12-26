import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RegistrationDetailsComponent} from './registration-details/registration-details.component';
import {MaterialModule} from "../material/material.module";
import {TranslateModule} from "@ngx-translate/core";
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
import {RegistrationModerationComponent} from './registration-moderation/registration-moderation.component';


@NgModule({
  declarations: [
    RegistrationDetailsComponent,
    RegistrationParticipateDialogComponent,
    RegistrationEditDialogComponent,
    RegistrationCancelDialogComponent,
    RegistrationParticipateAccountDialogComponent,
    RegistrationParticipateManualDialogComponent,
    RegistrationStatusComponent,
    RegistrationModerationComponent
  ],
  exports: [
    RegistrationDetailsComponent,
    RegistrationStatusComponent,
    RegistrationModerationComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    TranslateModule,
    ReactiveFormsModule,
    AccountModule
  ]
})
export class RegistrationModule {
}

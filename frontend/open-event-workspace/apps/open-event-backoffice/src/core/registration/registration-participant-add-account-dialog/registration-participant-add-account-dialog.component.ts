import {Component, inject} from '@angular/core';
import {AccountSearchEntry, Participant, Registration} from "@open-event-workspace/core";
import {MAT_DIALOG_DATA, MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle} from "@angular/material/dialog";
import {RegistrationService} from "@open-event-workspace/backoffice";
import {MatButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {TranslatePipe} from "@ngx-translate/core";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {AccountSelectComponent} from "../../account/account-select/account-select.component";
import {MatFormField, MatInput} from "@angular/material/input";
import {MatLabel} from "@angular/material/form-field";

@Component({
  selector: 'app-registration-participant-add-account-dialog',
  imports: [
    MatButton,
    MatDialogActions,
    MatDialogContent,
    MatDialogTitle,
    MatIcon,
    TranslatePipe,
    ReactiveFormsModule,
    AccountSelectComponent,
    MatFormField,
    MatInput,
    MatFormField,
    MatLabel
  ],
  templateUrl: './registration-participant-add-account-dialog.component.html',
  styleUrl: './registration-participant-add-account-dialog.component.scss'
})
export class RegistrationParticipantAddAccountDialogComponent {

  data: { registration: Registration, participant: Participant } = inject(MAT_DIALOG_DATA)
  account: AccountSearchEntry | undefined
  fg: FormGroup

  constructor(
    fb: FormBuilder,
    private service: RegistrationService,
    public dialogRef: MatDialogRef<RegistrationParticipantAddAccountDialogComponent>
  ) {
    this.fg = fb.group({
      size: [0, Validators.compose([Validators.required, Validators.min(1)])]
    })
  }

  onCancelClick(): void {
    this.dialogRef.close(null)
  }

  onSaveClick() {
    if (!this.fg.valid || !this.account) return
    let request = this.fg.value
    let accountId = this.account.id

    this.service.addParticipantAccount(this.data.registration.id, accountId, request).subscribe(
      {
        next: val => this.dialogRef.close(val),
        error: err => this.dialogRef.close(null),
      }
    )
  }

  handleAccountSelectionChanged(entry: AccountSearchEntry) {
    this.account = entry
  }
}

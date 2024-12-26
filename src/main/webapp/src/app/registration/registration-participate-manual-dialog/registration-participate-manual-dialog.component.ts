import {Component} from '@angular/core';
import {FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators} from "@angular/forms";
import {MatDialogRef} from "@angular/material/dialog";
import {ParticipantAddRequest} from "../model/registration-api";

@Component({
    selector: 'app-registration-participate-manual-dialog',
    templateUrl: './registration-participate-manual-dialog.component.html',
    styleUrls: ['./registration-participate-manual-dialog.component.scss'],
    standalone: false
})
export class RegistrationParticipateManualDialogComponent {

  fg: FormGroup

  constructor(
    public dialogRef: MatDialogRef<RegistrationParticipateManualDialogComponent>,
    private fb: FormBuilder
  ) {
    this.fg = fb.group({
        firstName: ['', Validators.required],
        lastName: ['', Validators.required],
        email: ['', Validators.email],
        phone: [''],
        mobile: [''],
        size: [0, Validators.compose([Validators.required, Validators.min(1)])]
      },
      {
        validators: [atLeastOneRequiredValidator(['email', 'phone', 'mobile'])]
      });
  }

  submit() {
    if (!this.fg.valid) return
    let value = this.fg.value
    this.dialogRef.close(new ParticipantAddRequest(value.firstName, value.lastName, value.email, value.phone, value.mobile, value.size))
  }
}


export function atLeastOneRequiredValidator(controlNames: string[]): ValidatorFn {
  // @ts-ignore
  return (formGroup: FormGroup): ValidationErrors | null => {
    const isValid = controlNames.some(controlName => {
      const control = formGroup.get(controlName);
      return control && control.value;
    });

    // Mark all controls as touched and invalid if none are set
    controlNames.forEach(controlName => {
      const control = formGroup.get(controlName);
      if (control) {
        control.setErrors(isValid ? null : { required: true });
      }
    });

    return isValid ? null : {atLeastOneRequired: true};
  };
}



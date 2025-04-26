import {Component, effect, input, output} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {TranslatePipe} from "@ngx-translate/core";
import {AccountSearchEntry} from "../../search/search.api";
import {AccountChangeRequest, AccountDetails, AccountSetupRequest} from "../account.api";
import {ProfileChangeRequest} from "../../profile/profile.api";

@Component({
  selector: 'lib-account-change',
  imports: [
    FormsModule,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule,
    TranslatePipe
  ],
  templateUrl: './account-change.component.html',
  styleUrl: './account-change.component.scss'
})
export class AccountChangeComponent {

  data = input<AccountSearchEntry | AccountDetails>()
  request = output<AccountSetupRequest>()

  fg: FormGroup

  constructor(fb: FormBuilder) {
    this.fg = fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.compose([Validators.email])],
      phone: [''],
      mobile: [''],
    })


    effect(() => {
      let account = this.data()
      if(account) this.handleDataChanged(account)
    });
  }

  private handleDataChanged(account: AccountSearchEntry | AccountDetails) {
    this.fg.get('firstName')?.setValue(account.firstName)
    this.fg.get('lastName')?.setValue(account.lastName)
    this.fg.get('email')?.setValue(account.email)
    this.fg.get('phone')?.setValue(account.phone)
    this.fg.get('mobile')?.setValue(account.mobile)
  }

  submit() {
    if (!this.fg.valid) return
    let value = this.fg.value
    let name = value.firstName + ' ' + value.lastName
    let request = new AccountSetupRequest(
      new AccountChangeRequest(name, '', undefined),
      new ProfileChangeRequest(
        value.email,
        value.phone,
        value.mobile,
        value.firstName,
        value.lastName,
        undefined,
        undefined,
        undefined,
        undefined,
        '')
    )
    this.request.emit(request)
  }
}

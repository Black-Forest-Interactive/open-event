import { Component, effect, inject, input, output } from '@angular/core'
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms'
import { MatFormField, MatLabel } from '@angular/material/form-field'
import { MatInput } from '@angular/material/input'
import { TranslatePipe } from '@ngx-translate/core'
import { AccountSearchEntry, AccountChangeRequest, AccountDetails, AccountSetupRequest, ProfileChangeRequest } from '@open-event/core'

@Component({
  selector: 'lib-account-change',
  imports: [FormsModule, MatFormField, MatInput, MatLabel, ReactiveFormsModule, TranslatePipe],
  templateUrl: './account-change.component.html',
  styleUrl: './account-change.component.scss'
})
export class AccountChangeComponent {
  data = input<AccountSearchEntry | AccountDetails>()
  request = output<AccountSetupRequest>()

  fg: FormGroup

  constructor() {
    const fb = inject(FormBuilder)

    this.fg = fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.compose([Validators.email])],
      phone: [''],
      mobile: ['']
    })

    effect(() => {
      const account = this.data()
      if (account) this.handleDataChanged(account)
    })
  }

  submit() {
    if (!this.fg.valid) return
    const value = this.fg.value
    const name = value.firstName + ' ' + value.lastName
    const request = new AccountSetupRequest(
      new AccountChangeRequest(name, '', undefined),
      new ProfileChangeRequest(value.email, value.phone, value.mobile, value.firstName, value.lastName, undefined, undefined, undefined, undefined, '')
    )
    this.request.emit(request)
  }

  private handleDataChanged(account: AccountSearchEntry | AccountDetails) {
    this.fg.get('firstName')?.setValue(account.firstName)
    this.fg.get('lastName')?.setValue(account.lastName)
    this.fg.get('email')?.setValue(account.email)
    this.fg.get('phone')?.setValue(account.phone)
    this.fg.get('mobile')?.setValue(account.mobile)
  }
}

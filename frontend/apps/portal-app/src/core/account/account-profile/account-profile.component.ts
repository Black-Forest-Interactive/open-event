import { Component, effect, inject, resource, signal } from '@angular/core'
import { ProfileChangeRequest } from '@open-event/core'
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { HotToastService } from '@ngxpert/hot-toast'
import { MatIcon } from '@angular/material/icon'
import { MatCard } from '@angular/material/card'
import { MatFormField, MatInput } from '@angular/material/input'
import { MatOption, MatSelect } from '@angular/material/select'
import { MatButton, MatIconButton } from '@angular/material/button'
import { LoadingBarComponent, toPromise } from '@open-event/shared'
import { AccountService } from '@open-event/portal'

@Component({
  selector: 'portal-account-profile',
  imports: [TranslatePipe, MatIcon, MatCard, ReactiveFormsModule, MatInput, MatFormField, MatSelect, MatOption, MatButton, MatIconButton, LoadingBarComponent],
  templateUrl: './account-profile.component.html',
  styleUrl: './account-profile.component.scss'
})
export class AccountProfileComponent {
  readonly editMode = signal(false)
  private service = inject(AccountService)
  private translate = inject(TranslateService)
  private toast = inject(HotToastService)
  private fb = inject(FormBuilder)
  readonly fg: FormGroup = this.fb.group({
    email: this.fb.control('', Validators.email),
    phone: this.fb.control(''),
    mobile: this.fb.control(''),
    firstName: this.fb.control('', Validators.required),
    lastName: this.fb.control('', Validators.required),
    dateOfBirth: this.fb.control(''),
    gender: this.fb.control(''),
    profilePicture: this.fb.control(''),
    website: this.fb.control(''),
    language: this.fb.control('')
  })
  private profileResource = resource({
    loader: (p) => toPromise(this.service.getProfile(), p.abortSignal)
  })
  readonly profile = this.profileResource.value
  readonly loading = this.profileResource.isLoading

  constructor() {
    effect(() => {
      const d = this.profile()
      if (!d) return
      this.fg.setValue({
        email: d.email,
        phone: d.phone,
        mobile: d.mobile,
        firstName: d.firstName,
        lastName: d.lastName,
        dateOfBirth: d.dateOfBirth,
        gender: d.gender,
        profilePicture: d.profilePicture,
        website: d.website,
        language: d.language
      })
      this.translate.use(d.language)
    })
  }

  save() {
    if (this.fg.dirty) {
      const request = this.fg.value as ProfileChangeRequest
      this.service.updateProfile(request).subscribe({
        next: (d) => {
          this.profileResource.set(d)
          this.translate.use(d.language)
        },
        error: () => this.toast.error()
      })
    }
    this.editMode.set(false)
  }

  cancel() {
    const d = this.profile()
    if (d) {
      this.fg.setValue({
        email: d.email,
        phone: d.phone,
        mobile: d.mobile,
        firstName: d.firstName,
        lastName: d.lastName,
        dateOfBirth: d.dateOfBirth,
        gender: d.gender,
        profilePicture: d.profilePicture,
        website: d.website,
        language: d.language
      })
    }
    this.fg.markAsPristine()
    this.editMode.set(false)
  }
}

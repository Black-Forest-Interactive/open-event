import {Component} from '@angular/core';
import {Profile, ProfileChangeRequest} from "../../profile/model/profile-api";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ProfileService} from "../../profile/model/profile.service";
import {AccountService} from "../model/account.service";
import {AuthService} from "../../auth/auth.service";
import {TranslateService} from "@ngx-translate/core";
import {HotToastService} from "@ngxpert/hot-toast";

@Component({
    selector: 'app-account-profile',
    templateUrl: './account-profile.component.html',
    styleUrl: './account-profile.component.scss',
    standalone: false
})
export class AccountProfileComponent {
  profile: Profile | undefined
  reloading: boolean = false
  editMode: boolean = false

  fg: FormGroup

  constructor(
    private fb: FormBuilder,
    private service: ProfileService,
    private accountService: AccountService,
    public authService: AuthService,
    private translate: TranslateService,
    private toast: HotToastService
  ) {
    this.fg = this.fb.group({
      email: this.fb.control('', Validators.email),
      phone: this.fb.control(''),
      mobile: this.fb.control(''),
      firstName: this.fb.control('', Validators.required),
      lastName: this.fb.control('', Validators.required),
      dateOfBirth: this.fb.control(''),
      gender: this.fb.control(''),
      profilePicture: this.fb.control(''),
      website: this.fb.control(''),
      language: this.fb.control(''),
    })
  }

  ngOnInit() {
    this.reload()
  }

  save() {
    if (this.fg.dirty && this.profile) {
      let request = this.fg.value as ProfileChangeRequest
      this.reloading = true
      this.service.updateProfile(this.profile.id, request).subscribe({
        next: d => {
          this.translate.use(d.language)
          this.handleData(d)
        },
        error: err => this.handleError(err)
      })
    }
    this.editMode = false
  }

  private reload() {
    if (this.reloading) return
    this.reloading = true
    this.accountService.getProfile().subscribe(d => this.handleData(d))
  }

  private handleData(d: Profile) {
    this.profile = d
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
    this.reloading = false
  }

  private handleError(err: any) {
    this.toast.error("Somethong went wrong")
  }
}

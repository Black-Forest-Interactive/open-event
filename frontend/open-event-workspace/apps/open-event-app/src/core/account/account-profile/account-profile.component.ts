import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Profile, ProfileChangeRequest} from "@open-event-workspace/core";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {HotToastService} from "@ngxpert/hot-toast";
import {MatIcon} from "@angular/material/icon";
import {MatCard} from "@angular/material/card";
import {MatDivider} from "@angular/material/divider";
import {MatList, MatListItem} from "@angular/material/list";
import {MatFormField, MatInput} from "@angular/material/input";
import {MatOption, MatSelect} from "@angular/material/select";
import {MatButton, MatMiniFabButton} from "@angular/material/button";
import {LoadingBarComponent} from "@open-event-workspace/shared";
import {AccountService} from "@open-event-workspace/app";

@Component({
  selector: 'app-account-profile',
  imports: [CommonModule, TranslatePipe, MatIcon, MatCard, MatDivider, MatListItem, MatList, ReactiveFormsModule, MatInput, MatFormField, MatSelect, MatOption, MatButton, MatMiniFabButton, LoadingBarComponent],
  templateUrl: './account-profile.component.html',
  styleUrl: './account-profile.component.scss',
})
export class AccountProfileComponent {
  profile: Profile | undefined
  reloading: boolean = false
  editMode: boolean = false

  fg: FormGroup

  constructor(
    private fb: FormBuilder,
    private service: AccountService,
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
      this.service.updateProfile(request).subscribe({
        next: d => this.handleData(d),
        error: err => this.handleError(err)
      })
    }
    this.editMode = false
  }

  private reload() {
    if (this.reloading) return
    this.reloading = true
    this.service.getProfile().subscribe(d => this.handleData(d))
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
    this.translate.use(d.language)
    this.reloading = false
  }

  private handleError(err: any) {
    this.toast.error("Somethong went wrong")
  }
}

import {Component, OnInit, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TranslatePipe} from "@ngx-translate/core";
import {MatCardModule} from "@angular/material/card";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {MatDivider} from "@angular/material/divider";
import {LoadingBarComponent} from "@open-event-workspace/shared";
import {AccountService} from "@open-event-workspace/app";
import {Preferences, PreferencesChangeRequest} from "@open-event-workspace/core";
import {HotToastService} from "@ngxpert/hot-toast";

@Component({
  selector: 'app-account-preferences',
  imports: [CommonModule, TranslatePipe, MatCardModule, MatButtonModule, MatIconModule, MatDivider, LoadingBarComponent],
  templateUrl: './account-preferences.component.html',
  styleUrl: './account-preferences.component.scss',
})
export class AccountPreferencesComponent implements OnInit {

  preferences = signal<Preferences | undefined>(undefined)
  reloading = signal(false)

  constructor(private service: AccountService, private toast: HotToastService) {
  }

  ngOnInit() {
    this.reload()
  }

  private reload() {
    if (this.reloading()) return
    this.reloading.set(true)
    this.service.getPreferences().subscribe({
      next: value => this.handleData(value),
      error: err => this.handleError(err),
      complete: () => this.reloading.set(false)
    })
  }

  private handleData(value: Preferences) {
    this.preferences.set(value)
    this.reloading.set(false)
  }

  private handleError(err: any) {
    if (err) this.toast.error(err)
    this.reloading.set(false)
  }

  subscribe() {
    this.update(true)
  }

  unsubscribe() {
    this.update(false)
  }

  private update(enabled: boolean) {
    if (this.reloading()) return
    this.reloading.set(true)
    const request = new PreferencesChangeRequest(
      {enabled: enabled},
      {enabled: this.preferences()?.communicationPreferences?.enabled ?? false},
      {enabled: this.preferences()?.notificationPreferences?.enabled ?? false}
    )
    this.service.updatePreferences(request).subscribe({
      next: value => this.handleData(value),
      error: err => this.handleError(err),
      complete: () => this.reloading.set(false)
    })
  }
}

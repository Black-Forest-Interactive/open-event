import {Component, Input} from '@angular/core';
import {AuthService, LoadingBarComponent} from "@open-event-workspace/shared";
import {MatSlideToggle, MatSlideToggleChange} from "@angular/material/slide-toggle";
import {Event, Share, ShareChangeRequest} from "@open-event-workspace/core";
import {MatCard} from "@angular/material/card";
import {TranslatePipe} from "@ngx-translate/core";
import {MatDivider} from "@angular/material/divider";
import {ShareButtons} from "ngx-sharebuttons/buttons";
import {ShareService} from "@open-event-workspace/app";
import {Roles} from "../../../shared/roles";

@Component({
  selector: 'app-share-details',
  templateUrl: './share-details.component.html',
  styleUrl: './share-details.component.scss',
  imports: [
    MatCard,
    TranslatePipe,
    MatSlideToggle,
    MatDivider,
    ShareButtons,
    LoadingBarComponent
  ],
  standalone: true
})
export class ShareDetailsComponent {

  @Input()
  set data(value: Event) {
    this.event = value
    this.updateData()
  }

  event: Event | undefined
  share: Share | undefined
  reloading: boolean = false
  adminOrManager: boolean = false

  constructor(
    private service: ShareService,
    private authService: AuthService
  ) {
  }

  ngOnInit() {
    this.adminOrManager = this.authService.hasRole(Roles.REGISTRATION_MANAGE, Roles.REGISTRATION_ADMIN)
    let principal = this.authService.getPrincipal()
    if (principal) this.adminOrManager = principal.roles.find(r => r === 'openevent.registration.manage' || r === 'openevent.registration.admin') != null
  }

  private updateData() {
    if (!this.event) return
    this.reloading = true
    this.service.getShareForEvent(this.event).subscribe({
      next: value => this.handleData(value),
      error: err => this.handleError(err)
    })
  }

  private handleData(d: Share) {
    this.share = d
    this.reloading = false
  }

  private handleError(err: any) {
    this.reloading = false
  }

  handleToggleChanged(event: MatSlideToggleChange) {
    if (event.checked) {
      this.enableSharing()
    } else {
      this.disableSharing()
    }
  }

  private enableSharing() {
    if (!this.event) return
    if (this.share && !this.share.published) {
      this.reloading = true
      this.service.publish(this.share.id).subscribe(
        {
          next: value => this.handleData(value),
          error: err => this.handleError(err)
        }
      )
    } else if (!this.share) {
      this.reloading = true
      this.service.createShare(new ShareChangeRequest(this.event.id, true)).subscribe(
        {
          next: value => this.handleData(value),
          error: err => this.handleError(err)
        }
      )
    }
  }

  private disableSharing() {
    if (!this.event) return
    if (!this.share) return
    this.reloading = true
    this.service.updateShare(this.share.id, new ShareChangeRequest(this.event.id, false)).subscribe(
      {
        next: value => this.handleData(value),
        error: err => this.handleError(err)
      }
    )
  }
}

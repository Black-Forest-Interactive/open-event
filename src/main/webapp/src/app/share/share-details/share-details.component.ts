import {Component, Input} from '@angular/core';
import {Event} from "../../event/model/event-api";
import {AuthService} from "../../auth/auth.service";
import {ShareService} from "../model/share.service";
import {Share, ShareChangeRequest} from "../model/share-api";
import {MatSlideToggleChange} from "@angular/material/slide-toggle";

@Component({
    selector: 'app-share-details',
    templateUrl: './share-details.component.html',
    styleUrl: './share-details.component.scss',
    standalone: false
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
    this.adminOrManager = this.authService.hasRole(AuthService.REGISTRATION_MANAGE, AuthService.REGISTRATION_ADMIN)
    let principal = this.authService.getPrincipal()
    if (principal) this.adminOrManager = principal.roles.find(r => r === 'openevent.registration.manage' || r === 'openevent.registration.admin') != null
  }

  private updateData() {
    if (!this.event) return
    this.reloading = true
    this.service.findByEvent(this.event.id).subscribe({
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

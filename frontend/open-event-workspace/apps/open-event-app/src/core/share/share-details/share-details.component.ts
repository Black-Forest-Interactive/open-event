import {Component, computed, input, resource, signal} from '@angular/core';
import {LoadingBarComponent, toPromise} from "@open-event-workspace/shared";
import {Event, Share, ShareChangeRequest} from "@open-event-workspace/core";
import {MatCard} from "@angular/material/card";
import {TranslatePipe} from "@ngx-translate/core";
import {MatDivider} from "@angular/material/divider";
import {ShareButtons} from "ngx-sharebuttons/buttons";
import {ShareService} from "@open-event-workspace/app";
import {Observable} from "rxjs";
import {HotToastService} from "@ngxpert/hot-toast";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-share-details',
  templateUrl: './share-details.component.html',
  styleUrl: './share-details.component.scss',
  imports: [
    MatCard,
    TranslatePipe,
    MatDivider,
    ShareButtons,
    LoadingBarComponent,
    MatButton
  ],
  standalone: true
})
export class ShareDetailsComponent {

  event = input.required<Event>()
  shareResource = resource({
    request: this.event,
    loader: (param) => {
      return toPromise(this.service.getShareForEvent(param.request))
    }
  })


  share = computed(this.shareResource.value ?? undefined)
  loading = this.shareResource.isLoading
  updating = signal(false)
  reloading = computed(() => this.loading() || this.updating())
  active = computed(() => this.share() && this.share()?.published)

  error = this.shareResource.error


  constructor(
    private service: ShareService,
    private toast: HotToastService
  ) {
  }

  enableSharing() {
    let share = this.share()
    if (share && !share.published) {
      this.update(this.service.publish(share.id))
    } else if (!this.share) {
      this.update(this.service.createShare(new ShareChangeRequest(this.event().id, true)))
    }
  }

  disableSharing() {
    let share = this.share()
    if (!share) return
    this.update(this.service.updateShare(share.id, new ShareChangeRequest(this.event().id, false)))
  }

  private update(observable: Observable<Share>) {
    this.updating.set(true)
    observable.subscribe(
      {
        next: value => this.handleData(value),
        error: err => this.handleError(err),
        complete: () => this.updating.set(false)
      }
    )
  }

  private handleData(d: Share) {
    this.shareResource.set(d)
  }

  private handleError(err: any) {
    this.toast.error("Something went wrong")
  }
}

import {Component, computed, input, output} from '@angular/core';
import {LoadingBarComponent} from "@open-event-workspace/shared";
import {EventInfo} from "@open-event-workspace/core";
import {MatCard} from "@angular/material/card";
import {TranslatePipe} from "@ngx-translate/core";
import {MatDivider} from "@angular/material/divider";
import {ShareButtons} from "ngx-sharebuttons/buttons";
import {MatButtonModule} from "@angular/material/button";

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
    MatButtonModule
  ],
  standalone: true
})
export class ShareDetailsComponent {

  info = input.required<EventInfo>()
  reloading = input(false)
  share = computed(() => this.info().share)
  active = computed(() => this.share() && this.share()!!.share.enabled)
  title = computed(() => this.info().event.title)
  url = computed(() => this.share()?.url ?? '')

  changed = output<boolean>()

  constructor() {
  }

  enableSharing() {
    this.changed.emit(true)
  }

  disableSharing() {
    this.changed.emit(false)
  }

}

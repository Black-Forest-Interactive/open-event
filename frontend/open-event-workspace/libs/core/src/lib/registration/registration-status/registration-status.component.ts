import {Component, input, Input} from '@angular/core';
import {EventSearchEntry, Participant, RegistrationInfo} from "@open-event-workspace/core";
import {TranslatePipe} from "@ngx-translate/core";
import {NgClass} from "@angular/common";
import {PublicEvent} from "@open-event-workspace/external";

@Component({
  selector: 'lib-registration-status',
  imports: [TranslatePipe, NgClass],
  templateUrl: './registration-status.component.html',
  styleUrl: './registration-status.component.scss'
})
export class RegistrationStatusComponent {

  maxIndicatorSize = input(10)

  spaceAvailable: boolean = false
  space = {
    remaining: 0,
    available: 0
  }

  indicator: any[] = []

  @Input()
  set data(info: RegistrationInfo | undefined) {
    if (info) {
      let totalAmount = info.participants.filter(p => !p.waitingList).reduce((sum: number, p: Participant) => sum + p.size, 0)
      this.space.available = info.registration.maxGuestAmount
      this.space.remaining = this.space.available - totalAmount
      this.spaceAvailable = this.space.remaining > 0
      this.updateIndicator()
    }
  }


  @Input()
  set entry(entry: EventSearchEntry) {
    this.spaceAvailable = entry.hasSpaceLeft
    this.space.remaining = entry.remainingSpace
    this.space.available = entry.maxGuestAmount
    this.updateIndicator()
  }

  @Input()
  set public(entry: PublicEvent) {
    this.spaceAvailable = entry.hasSpaceLeft
    this.space.remaining = entry.remainingSpace
    this.space.available = entry.maxGuestAmount
    this.updateIndicator()
  }

  private updateIndicator() {
    if (this.maxIndicatorSize() <= 0) return
    if (this.space.available >= this.maxIndicatorSize() || !this.spaceAvailable) {
      this.indicator = []
    } else {
      this.indicator = Array.from({length: this.space.available}, (_, i) => (i < this.space.remaining ? {key: i, value: 'bg-green-500'} : {key: i, value: 'bg-orange-300'}))
    }
  }

}

import { Component, computed, input, output } from '@angular/core'
import { EventInfo, Participant } from '@open-event/core'
import { RegistrationStatusComponent } from '@open-event/ui'
import { TranslatePipe } from '@ngx-translate/core'
import { MatButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { RouterLink } from '@angular/router'
import { EventActionExportComponent } from '../../event/event-action-export/event-action-export.component'

@Component({
  selector: 'portal-registration-details',
  templateUrl: './registration-details.component.html',
  styleUrl: './registration-details.component.scss',
  imports: [RegistrationStatusComponent, TranslatePipe, MatButton, MatIcon, RouterLink, EventActionExportComponent],
  standalone: true
})
export class RegistrationDetailsComponent {
  info = input.required<EventInfo>()
  userParticipant = input<Participant | undefined>(undefined)
  reloading = input(false)

  participate = output<void>()
  edit = output<void>()
  cancelParticipation = output<void>()

  readonly event = computed(() => this.info().event)
  readonly canEdit = computed(() => this.info().canEdit)
  readonly registration = computed(() => this.info().registration)
  readonly taken = computed(() => this.registration()?.participants.filter((p) => !p.waitingList).reduce((sum, p) => sum + p.size, 0) ?? 0)
  readonly capacity = computed(() => this.registration()?.registration.maxGuestAmount ?? 0)
  readonly isFull = computed(() => this.taken() >= this.capacity())
}

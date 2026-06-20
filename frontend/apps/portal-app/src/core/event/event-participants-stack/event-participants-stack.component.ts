import { Component, computed, input } from '@angular/core'
import { EventInfo, Participant } from '@open-event/core'
import { AccountDisplayNamePipe, AvatarComponent } from '@open-event/ui'
import { TranslatePipe } from '@ngx-translate/core'
import { MatIcon } from '@angular/material/icon'

const STACK_HUES = [153, 205, 30, 268, 350, 45]
const STACK_LIMIT = 6

@Component({
  selector: 'portal-event-participants-stack',
  templateUrl: './event-participants-stack.component.html',
  styleUrl: './event-participants-stack.component.scss',
  imports: [AvatarComponent, AccountDisplayNamePipe, TranslatePipe, MatIcon],
  standalone: true
})
export class EventParticipantsStackComponent {
  info = input.required<EventInfo>()
  userParticipant = input<Participant | undefined>(undefined)

  private registration = computed(() => this.info().registration)
  private accepted = computed(() => this.registration()?.participants.filter((p) => !p.waitingList) ?? [])

  readonly capacity = computed(() => this.registration()?.registration.maxGuestAmount ?? 0)
  readonly totalPersons = computed(() => this.accepted().reduce((sum, p) => sum + p.size, 0))
  readonly stackHues = computed(() => {
    const n = Math.min(this.totalPersons(), STACK_LIMIT)
    return Array.from({ length: n }, (_, i) => STACK_HUES[i % STACK_HUES.length])
  })
  readonly overflow = computed(() => Math.max(0, this.totalPersons() - STACK_LIMIT))
}

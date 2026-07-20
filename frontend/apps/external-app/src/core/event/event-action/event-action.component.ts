import { Component, computed, input, output, ChangeDetectionStrategy } from '@angular/core'
import { MatIcon } from '@angular/material/icon'
import { MatButtonModule } from '@angular/material/button'
import { TranslatePipe } from '@ngx-translate/core'
import { MatProgressSpinner } from '@angular/material/progress-spinner'

@Component({
  selector: 'app-event-action',
  imports: [MatIcon, MatButtonModule, MatProgressSpinner, TranslatePipe],
  templateUrl: './event-action.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './event-action.component.scss'
})
export class EventActionComponent {
  processing = input(false)
  status = input.required<string>()

  readonly participationPossible = computed(() => this.status() !== 'UNCONFIRMED' && !this.processing())

  participateEvent = output()
}

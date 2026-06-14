import { Component, input } from '@angular/core'
import { PublicEvent } from '@open-event/external'
import { DatePipe } from '@angular/common'
import { MatDivider } from '@angular/material/divider'
import { TranslatePipe } from '@ngx-translate/core'
import { RegistrationStatusComponent } from '@open-event/ui'

@Component({
  selector: 'app-event-info',
  imports: [DatePipe, MatDivider, TranslatePipe, RegistrationStatusComponent],
  templateUrl: './event-info.component.html',
  styleUrl: './event-info.component.scss'
})
export class EventInfoComponent {
  event = input.required<PublicEvent>()
}

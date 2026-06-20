import { Component, input, output } from '@angular/core'
import { AddressReadAPI, AudienceReadAPI, CategoryReadAPI, EventInfo } from '@open-event/core'
import { MatButton } from '@angular/material/button'
import { TranslatePipe } from '@ngx-translate/core'
import { MatIcon } from '@angular/material/icon'
import { FormGroup } from '@angular/forms'
import { EventChangeGeneralComponent } from '../event-change-general/event-change-general.component'
import { EventChangeLocationComponent } from '../event-change-location/event-change-location.component'
import { EventChangeRegistrationComponent } from '../event-change-registration/event-change-registration.component'

@Component({
  selector: 'lib-event-change-single',
  imports: [EventChangeGeneralComponent, EventChangeLocationComponent, EventChangeRegistrationComponent, MatButton, MatIcon, TranslatePipe],
  templateUrl: './event-change-single.component.html',
  styleUrl: './event-change-single.component.scss'
})
export class EventChangeSingleComponent {
  addressReadAPI = input.required<AddressReadAPI>()
  categoryReadAPI = input.required<CategoryReadAPI>()
  audienceReadAPI = input.required<AudienceReadAPI>()

  hiddenFields = input.required<string[]>()
  loading = input.required<boolean>()
  fg = input.required<FormGroup>()
  info = input<EventInfo>()
  submitLabel = input<string>('action.submit')

  submit = output<boolean>()
  cancel = output<boolean>()
}

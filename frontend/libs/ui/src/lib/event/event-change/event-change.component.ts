import { Component, effect, inject, input, output, signal } from '@angular/core'

import { MatCardModule } from '@angular/material/card'
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms'
import { TranslateModule, TranslateService } from '@ngx-translate/core'
import { AddressChangeRequest, AddressReadAPI, CategoryReadAPI, Event, EventChangeRequest, EventInfo, EventReadAPI, LocationChangeRequest, RegistrationChangeRequest } from '@open-event/core'
import { MatIconModule } from '@angular/material/icon'
import { MatButtonModule } from '@angular/material/button'
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner'
import { DateTime } from 'luxon'
import { LoadingBarComponent } from '@open-event/shared'
import { HotToastService } from '@ngxpert/hot-toast'
import { EventChangeSingleComponent } from '../event-change-single/event-change-single.component'

@Component({
  selector: 'lib-event-change',
  imports: [MatCardModule, MatIconModule, MatButtonModule, MatProgressSpinnerModule, ReactiveFormsModule, TranslateModule, LoadingBarComponent, EventChangeSingleComponent],
  templateUrl: './event-change.component.html',
  styleUrl: './event-change.component.scss'
})
export class EventChangeComponent {
  event = input<Event>()
  info = signal<EventInfo | undefined>(undefined)
  submitLabel = input<string>('action.submit')
  request = output<EventChangeRequest>()
  cancel = output<boolean>()

  hiddenFields: string[] = ['iconUrl', 'imageUrl', 'endDate', 'interestedAllowed', 'ticketsEnabled']

  loading: boolean = false

  addressReadAPI = input.required<AddressReadAPI>()
  categoryReadAPI = input.required<CategoryReadAPI>()
  eventReadAPI = input.required<EventReadAPI>()

  fg: FormGroup

  private translateService = inject(TranslateService)
  private toast = inject(HotToastService)

  constructor() {
    const fb = inject(FormBuilder)

    this.fg = fb.group({})

    effect(() => {
      const event = this.event()
      if (event) this.loadEventInfo(event)
    })
  }

  submit() {
    if (!this.fg.valid) return
    const value = this.fg.value
    const request = this.createRequest(value, this.isEndHidden())

    if (!request) return
    this.loading = true
    this.saveNewAddress(value.location)
    this.request.emit(request)
  }

  private saveNewAddress(location: any) {
    if (location.addressMode !== 'new' || !location.saveAddress) return
    const createAddress = this.addressReadAPI().createAddress
    if (!createAddress) return

    const request = new AddressChangeRequest(location.street, location.streetNumber, location.zip, location.city, location.country, location.additionalInfo, 0, 0)
    createAddress(request).subscribe({
      next: () => this.translateService.get('address.message.saved').subscribe((t) => this.toast.success(t)),
      error: () => this.translateService.get('address.message.error').subscribe((t) => this.toast.error(t))
    })
  }

  private isEndHidden() {
    return this.hiddenFields.find((f) => f === 'endDate') != null
  }

  private createRequest(value: any, endHidden: boolean): EventChangeRequest | undefined {
    const start = this.createDateTime(value.general.startTime, value.general.startDate)
    const end = endHidden ? this.createDateTime(value.general.endTime, value.general.startDate) : this.createDateTime(value.general.endTime, value.general.endDate)
    if (!start || !end) return undefined

    const location = new LocationChangeRequest(
      value.location.street,
      value.location.streetNumber,
      value.location.zip,
      value.location.city,
      value.location.country,
      value.location.additionalInfo,
      0.0,
      0.0,
      -1
    )
    const registration = new RegistrationChangeRequest(value.registration.maxGuestAmount, value.registration.interestedAllowed, value.registration.ticketsEnabled)

    return new EventChangeRequest(
      start.toFormat("yyyy-MM-dd'T'HH:mm:ss"),
      end.toFormat("yyyy-MM-dd'T'HH:mm:ss"),
      value.general.title,
      value.general.shortText,
      value.general.longText,
      value.general.imageUrl,
      value.general.iconUrl,
      value.registration.categories ?? [],
      location,
      registration,
      true,
      value.registration.shared,
      value.registration.tags ?? []
    )
  }

  private createDateTime(timeStr: string, date: DateTime): DateTime | undefined {
    const time = timeStr.split(':')
    if (time.length == 2 && date.isValid) {
      date = date.set({ hour: parseInt(time[0]), minute: parseInt(time[1]) })
      return date
    }
    return undefined
  }

  private loadEventInfo(event: Event) {
    this.loading = true
    this.eventReadAPI()
      .getEventInfo(event.id)
      .subscribe({
        next: (value) => this.info.set(value),
        complete: () => (this.loading = false)
      })
  }
}

import {Component, effect, input, output, signal} from '@angular/core';
import {CommonModule} from "@angular/common";
import {MatCardModule} from "@angular/material/card";
import {MatStepperModule, StepperOrientation} from "@angular/material/stepper";
import {FormBuilder, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {TranslateModule} from "@ngx-translate/core";
import {Event, EventChangeRequest, EventInfo, EventReadAPI} from "../event.api";
import {map, Observable} from "rxjs";
import {BreakpointObserver} from "@angular/cdk/layout";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {EventChangeGeneralComponent} from "../event-change-general/event-change-general.component";
import {EventChangeLocationComponent} from "../event-change-location/event-change-location.component";
import {AddressReadAPI} from "../../address/address.api";
import {CategoryReadAPI} from "../../category/category-api";
import {EventChangeRegistrationComponent} from "../event-change-registration/event-change-registration.component";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {LocationChangeRequest} from "../../location/location.api";
import {RegistrationChangeRequest} from "../../registration/registration.api";
import {DateTime} from 'luxon';
import {LoadingBarComponent} from "@open-event-workspace/shared";

@Component({
  selector: 'lib-event-change',
  imports: [CommonModule, MatCardModule, MatStepperModule, MatIconModule, MatButtonModule, MatProgressSpinnerModule, ReactiveFormsModule, TranslateModule, EventChangeGeneralComponent, EventChangeLocationComponent, EventChangeRegistrationComponent, LoadingBarComponent],
  templateUrl: './event-change.component.html',
  styleUrl: './event-change.component.scss'
})
export class EventChangeComponent {

  title = input.required<string>()
  event = input<Event>()
  info = signal<EventInfo | undefined>(undefined)
  request = output<EventChangeRequest>()
  cancel = output<boolean>()

  hiddenFields: string[] = ['shortText', 'iconUrl', 'imageUrl', 'endDate', 'interestedAllowed', 'ticketsEnabled']

  loading: boolean = false

  addressReadAPI = input.required<AddressReadAPI>()
  categoryReadAPI = input.required<CategoryReadAPI>()
  eventReadAPI = input.required<EventReadAPI>()

  stepperOrientation: Observable<StepperOrientation>

  fg: FormGroup

  constructor(
    fb: FormBuilder,
    breakpointObserver: BreakpointObserver
  ) {

    this.fg = fb.group({})

    this.stepperOrientation = breakpointObserver
      .observe('(min-width: 800px)')
      .pipe(map(({matches}) => (matches ? 'horizontal' : 'vertical')))

    effect(() => {
      let event = this.event()
      if (event) this.loadEventInfo(event)
    })
  }


  private isEndHidden() {
    return this.hiddenFields.find(f => f === 'endDate') != null
  }

  submit() {
    if (!this.fg.valid) return
    let value = this.fg.value
    let request = this.createRequest(value, this.isEndHidden())
    if (!request) return
    this.loading = true
    this.request.emit(request)
  }


  private createRequest(value: any, endHidden: boolean): EventChangeRequest | undefined {
    let start = this.createDateTime(value.general.startTime, value.general.startDate)
    let end = endHidden ? this.createDateTime(value.general.endTime, value.general.startDate) : this.createDateTime(value.general.endTime, value.general.endDate)
    if (!start || !end) return undefined

    let location = new LocationChangeRequest(
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
    let registration = new RegistrationChangeRequest(
      value.registration.maxGuestAmount,
      value.registration.interestedAllowed,
      value.registration.ticketsEnabled
    )


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
      value.registration.tags ?? []
    )
  }

  private createDateTime(timeStr: string, date: DateTime): DateTime | undefined {
    let time = timeStr.split(":");
    if (time.length == 2 && date.isValid) {
      date = date.set({hour: parseInt(time[0]), minute: parseInt(time[1])});
      return date
    }
    return undefined;
  }

  private loadEventInfo(event: Event) {
    this.loading = true
    this.eventReadAPI().getEventInfo(event.id).subscribe({
      next: value => this.info.set(value),
      complete: () => this.loading = false
    })
  }

}

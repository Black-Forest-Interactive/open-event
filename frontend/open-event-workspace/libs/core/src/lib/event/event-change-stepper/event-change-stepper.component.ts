import {Component, input, output} from '@angular/core';
import {map, Observable} from "rxjs";
import {MatStepperModule, StepperOrientation} from "@angular/material/stepper";
import {FormGroup, ReactiveFormsModule} from "@angular/forms";
import {BreakpointObserver} from "@angular/cdk/layout";
import {CommonModule} from "@angular/common";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {TranslateModule} from "@ngx-translate/core";
import {EventChangeLocationComponent} from "../event-change-location/event-change-location.component";
import {EventChangeRegistrationComponent} from "../event-change-registration/event-change-registration.component";
import {EventChangeGeneralComponent} from "../event-change-general/event-change-general.component";
import {AddressReadAPI} from "../../address";
import {CategoryReadAPI} from "../../category";
import {EventInfo} from "../event.api";

@Component({
  selector: 'lib-event-change-stepper',
  imports: [CommonModule, MatCardModule, MatStepperModule, MatIconModule, MatButtonModule, MatProgressSpinnerModule, ReactiveFormsModule, TranslateModule, EventChangeGeneralComponent, EventChangeLocationComponent, EventChangeRegistrationComponent],
  templateUrl: './event-change-stepper.component.html',
  styleUrl: './event-change-stepper.component.scss'
})
export class EventChangeStepperComponent {

  addressReadAPI = input.required<AddressReadAPI>()
  categoryReadAPI = input.required<CategoryReadAPI>()

  hiddenFields = input.required<string[]>()
  loading = input.required<boolean>()
  fg = input.required<FormGroup>()
  info = input<EventInfo>()

  submit = output<boolean>()
  cancel = output<boolean>()

  stepperOrientation: Observable<StepperOrientation>


  constructor(
    breakpointObserver: BreakpointObserver
  ) {
    this.stepperOrientation = breakpointObserver
      .observe('(min-width: 800px)')
      .pipe(map(({matches}) => (matches ? 'horizontal' : 'vertical')))
  }

}

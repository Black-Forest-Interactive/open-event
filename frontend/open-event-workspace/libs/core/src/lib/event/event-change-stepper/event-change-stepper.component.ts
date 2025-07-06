import {Component, input, output} from '@angular/core';
import {AddressReadAPI, CategoryReadAPI, EventChangeGeneralComponent, EventChangeLocationComponent, EventChangeRegistrationComponent, EventInfo} from "@open-event-workspace/core";
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
  info = input.required<EventInfo>()

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

import {Component, input, output} from '@angular/core';
import {AddressReadAPI, CategoryReadAPI, EventChangeGeneralComponent, EventChangeLocationComponent, EventChangeRegistrationComponent, EventInfo} from "@open-event-workspace/core";
import {MatButton} from "@angular/material/button";
import {TranslatePipe} from "@ngx-translate/core";
import {MatIcon} from "@angular/material/icon";
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'lib-event-change-single',
  imports: [
    EventChangeGeneralComponent,
    EventChangeLocationComponent,
    EventChangeRegistrationComponent,
    MatButton,
    MatIcon,
    TranslatePipe
  ],
  templateUrl: './event-change-single.component.html',
  styleUrl: './event-change-single.component.scss'
})
export class EventChangeSingleComponent {

  addressReadAPI = input.required<AddressReadAPI>()
  categoryReadAPI = input.required<CategoryReadAPI>()

  hiddenFields = input.required<string[]>()
  loading = input.required<boolean>()
  fg = input.required<FormGroup>()
  info = input.required<EventInfo>()


  submit = output<boolean>()
  cancel = output<boolean>()
}

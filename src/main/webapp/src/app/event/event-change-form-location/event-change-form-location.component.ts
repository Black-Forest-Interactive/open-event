import {Component, Input} from '@angular/core';
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'app-event-change-form-location',
  templateUrl: './event-change-form-location.component.html',
  styleUrls: ['./event-change-form-location.component.scss']
})
export class EventChangeFormLocationComponent {
  @Input() form: FormGroup | undefined
  @Input() hiddenFields: string[] = []

  isVisible(ctrl: string): boolean {
    return this.hiddenFields.find(x => x == ctrl) == null
  }
}

import {Component, Input} from '@angular/core';
import {FormGroup} from "@angular/forms";


@Component({
  selector: 'app-event-change-form-event',
  templateUrl: './event-change-form-event.component.html',
  styleUrls: ['./event-change-form-event.component.scss']
})
export class EventChangeFormEventComponent {
  @Input() form: FormGroup | undefined
  @Input() hiddenFields: string[] = []


  constructor() {
  }

  get imageUrl() {
    return this.form?.get('imageUrl');
  }

  get iconUrl() {
    return this.form?.get('iconUrl');
  }

  get longText() {
    return this.form?.get('longText');
  }

  get shortText() {
    return this.form?.get('shortText');
  }

  get title() {
    return this.form?.get('title');
  }


  isVisible(ctrl: string): boolean {
    return this.hiddenFields.find(x => x == ctrl) == null
  }
}

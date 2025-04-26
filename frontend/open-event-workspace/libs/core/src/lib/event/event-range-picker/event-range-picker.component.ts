import {Component, effect, input, output} from '@angular/core';
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {TranslatePipe} from "@ngx-translate/core";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {EventRangeSelection} from "./event-range-selection";

@Component({
  selector: 'lib-event-range-picker',
  imports: [MatFormFieldModule, MatDatepickerModule, TranslatePipe, ReactiveFormsModule],
  templateUrl: './event-range-picker.component.html',
  styleUrl: './event-range-picker.component.scss'
})
export class EventRangePickerComponent {
  fg: FormGroup
  required = input(true)
  rangeChanged = output<EventRangeSelection>()

  constructor(private fb: FormBuilder) {
    this.fg = this.fb.group({
      from: this.fb.control('', Validators.required),
      to: this.fb.control('', Validators.required),
    })

    effect(() => {
      let required = this.required()
      if (required) {
        this.fg.controls['from'].setValidators(Validators.required)
        this.fg.controls['to'].setValidators(Validators.required)
      } else {
        this.fg.controls['from'].setValidators([])
        this.fg.controls['to'].setValidators([])
      }
    });
  }

  onDateRangePickerClosed() {
    if (!this.fg.valid) return
    let value = this.fg.value as EventRangeSelection
    this.rangeChanged.emit(value)
  }


  clear() {
    this.fg.setValue({from: '', to: ''})
  }

}



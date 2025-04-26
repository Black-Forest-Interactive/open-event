import {Component, effect, input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {EventInfo} from "../event.api";
import {DateTime} from 'luxon';
import {CommonModule} from "@angular/common";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {TranslatePipe} from "@ngx-translate/core";
import {MatInputModule} from "@angular/material/input";

@Component({
  selector: 'lib-event-change-general',
  imports: [CommonModule, ReactiveFormsModule, MatFormFieldModule, MatDatepickerModule, MatInputModule, TranslatePipe],
  templateUrl: './event-change-general.component.html',
  styleUrl: './event-change-general.component.scss'
})
export class EventChangeGeneralComponent implements OnInit {

  data = input<EventInfo>()
  hiddenFields = input<string[]>([])
  parent = input.required<FormGroup>()
  fg: FormGroup

  constructor(fb: FormBuilder) {
    this.fg = fb.group({
      startTime: fb.control('', Validators.required),
      startDate: fb.control('', Validators.required),
      endTime: fb.control('', Validators.required),
      endDate: fb.control('', Validators.required),

      imageUrl: fb.control(''),
      iconUrl: fb.control(''),
      longText: fb.control(''),
      shortText: fb.control(''),
      title: fb.control('', Validators.required),
    })

    effect(() => {
      let event = this.data()
      if (event) this.handleDataChanged(event)
    })
    effect(() => {
      let parent = this.parent()
      parent.addControl("general", this.fg)
    })
  }

  ngOnInit() {

    let endDate = this.fg.get('endDate');
    if (endDate) endDate.validator = this.isEndHidden() ? null : Validators.required
  }

  private handleDataChanged(info: EventInfo) {
    let start = DateTime.fromISO(info.event.start)
    let startTime = start.toFormat("HH:mm")
    let finish = DateTime.fromISO(info.event.finish)
    let finishTime = finish.toFormat("HH:mm")

    this.fg.setValue(
      {
        startTime: startTime,
        startDate: start,
        endTime: finishTime,
        endDate: finish,

        imageUrl: info.event.imageUrl ?? "",
        iconUrl: info.event.iconUrl ?? "",
        longText: info.event.longText ?? "",
        shortText: info.event.shortText ?? "",
        title: info.event.title ?? "",
      }
    )
  }

  get imageUrl() {
    return this.fg.get('imageUrl');
  }

  get iconUrl() {
    return this.fg.get('iconUrl');
  }

  get longText() {
    return this.fg.get('longText');
  }

  get shortText() {
    return this.fg.get('shortText');
  }

  get title() {
    return this.fg.get('title');
  }

  isVisible(ctrl: string): boolean {
    return this.hiddenFields().find(x => x == ctrl) == null
  }

  private isEndHidden() {
    return this.hiddenFields().find(f => f === 'endDate') != null
  }
}

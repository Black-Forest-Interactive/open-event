import {Component, computed, effect, input, resource} from '@angular/core';
import {EventInfo} from "../event.api";
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {CommonModule} from "@angular/common";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatInputModule} from "@angular/material/input";
import {TranslatePipe} from "@ngx-translate/core";
import {MatSelectModule} from "@angular/material/select";
import {MatChipInputEvent, MatChipsModule} from "@angular/material/chips";
import {CategoryReadAPI} from "../../category/category-api";
import {ChipSelectPaneComponent, LoadingBarComponent, toPromise} from "@open-event-workspace/shared";
import {ChipSelectEntry} from "../../../../../shared/src/lib/chip-select-pane/chip-select-entry";
import {MatIconModule} from "@angular/material/icon";
import {MatCheckbox} from "@angular/material/checkbox";

@Component({
  selector: 'lib-event-change-registration',
  imports: [CommonModule, ReactiveFormsModule, MatFormFieldModule, MatDatepickerModule, MatInputModule, MatSelectModule, MatChipsModule, MatIconModule, TranslatePipe, LoadingBarComponent, ChipSelectPaneComponent, MatCheckbox],
  templateUrl: './event-change-registration.component.html',
  styleUrl: './event-change-registration.component.scss'
})
export class EventChangeRegistrationComponent {

  data = input<EventInfo>()
  hiddenFields = input<string[]>([])
  parent = input.required<FormGroup>()
  fg: FormGroup

  categoryReadAPI = input.required<CategoryReadAPI>()

  categoryResource = resource({
    loader: (param) => {
      return toPromise(this.categoryReadAPI().getAllCategories(0, 100))
    }
  })

  category = computed(this.categoryResource.value ?? undefined)
  allCategories = computed(() => this.category()?.content?.map(d => ({id: d.id, name: d.name} as ChipSelectEntry)) ?? [])
  loading = this.categoryResource.isLoading
  error = this.categoryResource.error

  constructor(fb: FormBuilder) {
    this.fg = fb.group({
      maxGuestAmount: [4, Validators.required],
      interestedAllowed: [false, Validators.required],
      ticketsEnabled: [false, Validators.required],
      shared: [true, Validators.required],
      categories: [[]],
      tags: fb.control([]),
    })

    effect(() => {
      let event = this.data()
      if (event) this.handleDataChanged(event)
    })

    effect(() => {
      let parent = this.parent()
      parent.addControl("registration", this.fg)
    });
  }


  private handleDataChanged(info: EventInfo) {
    let registration = info.registration
    if (registration) {
      this.fg.setValue({
        ticketsEnabled: registration.registration.ticketsEnabled,
        maxGuestAmount: registration.registration.maxGuestAmount,
        interestedAllowed: registration.registration.interestedAllowed,
        shared: info.share?.share.enabled ?? false,
        categories: info.categories.map(c => c.id) ?? [],
        tags: info.event.tags ?? [],
      })
    }
  }

  isVisible(ctrl: string): boolean {
    return this.hiddenFields().find(x => x == ctrl) == null
  }

  get ticketsEnabled(): FormControl<any> {
    // @ts-ignore
    return this.form!!.get('ticketsEnabled')
  }

  get categories(): FormControl {
    return this.fg.get('categories') as FormControl
  }

  addTag(event: MatChipInputEvent) {
    const value = (event.value || '').trim()
    if (value.length <= 0) return
    const t = this.tags
    if (value && t) {
      let data = t.value as string[]
      let index = data.indexOf(value)
      if (index < 0) data.push(value)
    }
    event.chipInput!.clear();
  }

  get tags(): FormControl {
    return this.fg.get('tags') as FormControl;
  }

  removeTag(tag: string) {
    let t = this.tags
    if (!t) return

    let index = (t.value as string[]).indexOf(tag)
    if (index >= 0) {
      (t.value as string[]).splice(index, 1)
    }
  }
}

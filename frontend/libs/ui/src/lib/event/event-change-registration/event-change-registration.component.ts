import { Component, computed, effect, inject, input, resource } from '@angular/core'
import { Category, CategoryReadAPI, EventInfo } from '@open-event/core'
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms'

import { MatFormFieldModule } from '@angular/material/form-field'
import { MatInputModule } from '@angular/material/input'
import { TranslatePipe } from '@ngx-translate/core'
import { MatSelectModule } from '@angular/material/select'
import { MatChipInputEvent, MatChipsModule } from '@angular/material/chips'
import { LoadingBarComponent, toPromise } from '@open-event/shared'
import { MatIconModule } from '@angular/material/icon'
import { MatCheckbox } from '@angular/material/checkbox'
import { CategoryPickerComponent } from '../../category/category-picker/category-picker.component'
import { StepperInputComponent } from '../../stepper-input/stepper-input.component'

@Component({
  selector: 'lib-event-change-registration',
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatChipsModule,
    MatIconModule,
    TranslatePipe,
    LoadingBarComponent,
    MatCheckbox,
    CategoryPickerComponent,
    StepperInputComponent
  ],
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
  allCategories = computed<Category[]>(() => this.category()?.content ?? [])
  loading = this.categoryResource.isLoading
  error = this.categoryResource.error

  readonly minCapacity = computed(() => {
    const participants = this.data()?.registration?.participants ?? []
    const taken = participants.filter((p) => !p.waitingList).reduce((sum, p) => sum + p.size, 0)
    return Math.max(1, taken)
  })

  constructor() {
    const fb = inject(FormBuilder)

    this.fg = fb.group({
      maxGuestAmount: [4, Validators.required],
      interestedAllowed: [false, Validators.required],
      ticketsEnabled: [false, Validators.required],
      shared: [true, Validators.required],
      categories: [[]],
      tags: fb.control([])
    })

    effect(() => {
      const event = this.data()
      if (event) this.handleDataChanged(event)
    })

    effect(() => {
      const parent = this.parent()
      parent.addControl('registration', this.fg)
    })
  }

  get maxGuestAmount(): FormControl {
    return this.fg.get('maxGuestAmount') as FormControl
  }

  get categories(): FormControl {
    return this.fg.get('categories') as FormControl
  }

  get tags(): FormControl {
    return this.fg.get('tags') as FormControl
  }

  isVisible(ctrl: string): boolean {
    return this.hiddenFields().find((x) => x == ctrl) == null
  }

  toggleCategory(id: number) {
    const current = (this.categories.value as number[]) ?? []
    const updated = current.includes(id) ? current.filter((c) => c !== id) : [...current, id]
    this.categories.setValue(updated)
  }

  addTag(event: MatChipInputEvent) {
    const value = (event.value || '').trim()
    if (value.length <= 0) return
    const t = this.tags
    if (value && t) {
      const data = t.value as string[]
      const index = data.indexOf(value)
      if (index < 0) data.push(value)
    }
    event.chipInput!.clear()
  }

  removeTag(tag: string) {
    const t = this.tags
    if (!t) return

    const index = (t.value as string[]).indexOf(tag)
    if (index >= 0) {
      ;(t.value as string[]).splice(index, 1)
    }
  }

  private handleDataChanged(info: EventInfo) {
    const registration = info.registration
    if (registration) {
      this.fg.setValue({
        ticketsEnabled: registration.registration.ticketsEnabled,
        maxGuestAmount: registration.registration.maxGuestAmount,
        interestedAllowed: registration.registration.interestedAllowed,
        shared: info.share?.share.enabled ?? false,
        categories: info.categories.map((c) => c.id) ?? [],
        tags: info.event.tags ?? []
      })
    }
  }
}

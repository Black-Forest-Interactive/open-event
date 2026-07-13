import { Component, computed, DestroyRef, effect, inject, resource, signal, ChangeDetectionStrategy } from '@angular/core'
import { Location } from '@angular/common'
import { Router } from '@angular/router'
import { FormBuilder, FormGroup } from '@angular/forms'
import { StepperSelectionEvent } from '@angular/cdk/stepper'
import { MatStepperModule } from '@angular/material/stepper'
import { MatButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { MatCard } from '@angular/material/card'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { HotToastService } from '@ngxpert/hot-toast'
import { DateTime } from 'luxon'
import { AddressChangeRequest, EventChangeRequest, LocationChangeRequest, RegistrationChangeRequest } from '@open-event/core'
import { AddressService, AudienceService, CategoryService, EventService } from '@open-event/portal'
import { toPromise, TourService } from '@open-event/shared'
import { CategoryChipComponent, EventChangeGeneralComponent, EventChangeLocationComponent, EventChangeRegistrationComponent } from '@open-event/ui'
import { eventCreateTours } from './event-create.tour'

interface WizardSummary {
  title: string
  shortText: string
  dateLine: string
  locationLine: string
  maxGuests: number
  audienceNames: string
  categoryNames: string[]
  tagsLine: string
  shared: boolean
}

@Component({
  selector: 'portal-event-create',
  imports: [MatStepperModule, MatButton, MatIcon, MatCard, TranslatePipe, CategoryChipComponent, EventChangeGeneralComponent, EventChangeLocationComponent, EventChangeRegistrationComponent],
  templateUrl: './event-create.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './event-create.component.scss'
})
export class EventCreateComponent {
  private service = inject(EventService)
  private addressService = inject(AddressService)
  private categoryService = inject(CategoryService)
  private audienceService = inject(AudienceService)
  private translate = inject(TranslateService)
  private toast = inject(HotToastService)
  private router = inject(Router)
  private location = inject(Location)
  private tourService = inject(TourService)
  private destroyRef = inject(DestroyRef)

  readonly generalHiddenFields = ['imageUrl', 'iconUrl', 'endDate']
  readonly registrationHiddenFields = ['interestedAllowed', 'ticketsEnabled']

  fg: FormGroup

  addressReadAPI = {
    getAllAddresses: (page: number, size: number) => this.addressService.getAddresses(page, size),
    getAddress: (id: number) => this.addressService.getAddress(id),
    createAddress: (request: AddressChangeRequest) => this.addressService.createAddress(request)
  }

  categoryReadAPI = {
    getAllCategories: (page: number, size: number) => this.categoryService.getCategories(page, size),
    getCategory: (id: number) => this.categoryService.getCategory(id)
  }

  audienceReadAPI = {
    getAllAudiences: (page: number, size: number) => this.audienceService.getAudiences(page, size),
    getAudience: (id: number) => this.audienceService.getAudience(id)
  }

  private categoriesResource = resource({ loader: (p) => toPromise(this.categoryService.getCategories(0, 100), p.abortSignal) })
  private audiencesResource = resource({ loader: (p) => toPromise(this.audienceService.getAudiences(0, 100), p.abortSignal) })
  private allCategories = computed(() => this.categoriesResource.value()?.content ?? [])
  private allAudiences = computed(() => this.audiencesResource.value()?.content ?? [])

  readonly stepIndex = signal(0)
  readonly summary = signal<WizardSummary | undefined>(undefined)
  readonly saving = signal(false)

  private defaultCategoryApplied = false
  private activeTour = eventCreateTours[0]

  constructor() {
    const fb = inject(FormBuilder)
    this.fg = fb.group({})

    this.tourService.register(this.activeTour)
    this.destroyRef.onDestroy(() => this.tourService.unregister(this.activeTour.id))

    effect(() => {
      const categories = this.allCategories()
      const control = this.fg.get('registration.categories')
      if (this.defaultCategoryApplied || !control || categories.length === 0) return
      this.defaultCategoryApplied = true
      const fallback = categories.find((c) => c.name === 'Gemeinschaft')
      if (fallback && (control.value ?? []).length === 0) control.setValue([fallback.id])
    })
  }

  get generalGroup(): FormGroup {
    return this.fg.get('general') as FormGroup
  }

  get locationGroup(): FormGroup {
    return this.fg.get('location') as FormGroup
  }

  get registrationGroup(): FormGroup {
    return this.fg.get('registration') as FormGroup
  }

  isCurrentStepValid(): boolean {
    const idx = this.stepIndex()
    if (idx === 0) return this.generalGroup?.valid ?? false
    if (idx === 1) return this.locationGroup?.valid ?? false
    return true
  }

  stepHint(): string {
    return this.stepIndex() === 0 ? 'event.wizard.hint.step1' : 'event.wizard.hint.step2'
  }

  handleStepChange(event: StepperSelectionEvent) {
    this.stepIndex.set(event.selectedIndex)
    if (event.selectedIndex === 3) this.summary.set(this.buildSummary())

    const tour = eventCreateTours[event.selectedIndex]
    if (tour && tour !== this.activeTour) {
      this.activeTour = tour
      const enteredAt = Date.now()
      // wait for the step transition animation before the tour positions its popover
      this.tourService.register(tour, () => Date.now() - enteredAt > 600)
    }
  }

  handleEnter(event: Event) {
    const target = event.target as HTMLElement
    if (target.tagName === 'TEXTAREA' || this.stepIndex() > 1) return
    event.preventDefault()
    if (this.isCurrentStepValid()) (event.currentTarget as HTMLElement).querySelector<HTMLElement>('.wizard-next')?.click()
  }

  back() {
    this.location.back()
  }

  save() {
    if (!this.fg.valid || this.saving()) return
    const value = this.fg.value
    const request = this.createRequest(value)
    if (!request) return
    this.saving.set(true)
    this.saveNewAddress(value.location)
    this.service.create(request).subscribe({
      next: () => {
        this.translate.get('event.message.create.saved').subscribe((t) => this.toast.success(t))
        this.router.navigate(['/event/own']).then()
      },
      error: () => {
        this.translate.get('event.message.create.failed').subscribe((t) => this.toast.error(t))
        this.saving.set(false)
      }
    })
  }

  private buildSummary(): WizardSummary {
    const value = this.fg.value
    const general = value.general ?? {}
    const location = value.location ?? {}
    const registration = value.registration ?? {}

    const locale = this.translate.currentLang || 'de'
    const date = general.startDate as DateTime
    const dateLine = date?.isValid ? `${date.setLocale(locale).toFormat('ccc, d. LLL')} · ${general.startTime}–${general.endTime}` : ''

    const selectedCategories = (registration.categories ?? []) as number[]
    const selectedAudiences = (registration.audiences ?? []) as number[]

    return {
      title: general.title ?? '',
      shortText: general.shortText ?? '',
      dateLine,
      locationLine: `${location.street ?? ''} ${location.streetNumber ?? ''}, ${location.zip ?? ''} ${location.city ?? ''}`,
      maxGuests: registration.maxGuestAmount ?? 0,
      audienceNames: this.allAudiences().filter((a) => selectedAudiences.includes(a.id)).map((a) => a.name).join(', '),
      categoryNames: this.allCategories().filter((c) => selectedCategories.includes(c.id)).map((c) => c.name),
      tagsLine: ((registration.tags ?? []) as string[]).join(' · '),
      shared: registration.shared ?? false
    }
  }

  private saveNewAddress(location: any) {
    if (location.addressMode !== 'new' || !location.saveAddress) return
    const request = new AddressChangeRequest(location.street, location.streetNumber, location.zip, location.city, location.country, location.additionalInfo, 0, 0)
    this.addressService.createAddress(request).subscribe({
      next: () => this.translate.get('address.message.saved').subscribe((t) => this.toast.success(t)),
      error: () => this.translate.get('address.message.error').subscribe((t) => this.toast.error(t))
    })
  }

  private createRequest(value: any): EventChangeRequest | undefined {
    const start = this.createDateTime(value.general.startTime, value.general.startDate)
    const end = this.createDateTime(value.general.endTime, value.general.startDate)
    if (!start || !end) return undefined

    const location = new LocationChangeRequest(value.location.street, value.location.streetNumber, value.location.zip, value.location.city, value.location.country, value.location.additionalInfo, 0.0, 0.0, -1)
    const registration = new RegistrationChangeRequest(value.registration.maxGuestAmount, value.registration.interestedAllowed, value.registration.ticketsEnabled)

    return new EventChangeRequest(
      start.toFormat("yyyy-MM-dd'T'HH:mm:ss"),
      end.toFormat("yyyy-MM-dd'T'HH:mm:ss"),
      value.general.title,
      value.general.shortText,
      value.general.longText,
      value.general.imageUrl,
      value.general.iconUrl,
      value.registration.categories ?? [],
      value.registration.audiences ?? [],
      location,
      registration,
      true,
      value.registration.shared,
      value.registration.tags ?? []
    )
  }

  private createDateTime(timeStr: string, date: DateTime): DateTime | undefined {
    const time = (timeStr ?? '').split(':')
    if (time.length == 2 && date?.isValid) return date.set({ hour: parseInt(time[0]), minute: parseInt(time[1]) })
    return undefined
  }
}

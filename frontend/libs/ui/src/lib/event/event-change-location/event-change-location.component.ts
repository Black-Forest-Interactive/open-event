import { Component, computed, effect, inject, input, OnInit, resource, signal } from '@angular/core'
import { Address, AddressReadAPI, EventInfo } from '@open-event/core'
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms'

import { MatFormFieldModule } from '@angular/material/form-field'
import { MatInputModule } from '@angular/material/input'
import { TranslatePipe } from '@ngx-translate/core'
import { MatButtonToggleModule } from '@angular/material/button-toggle'
import { MatRadioChange, MatRadioModule } from '@angular/material/radio'
import { MatCheckboxModule } from '@angular/material/checkbox'
import { MatIcon } from '@angular/material/icon'
import { toPromise } from '@open-event/shared'

@Component({
  selector: 'lib-event-change-location',
  imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule, TranslatePipe, MatButtonToggleModule, MatRadioModule, MatCheckboxModule, MatIcon],
  templateUrl: './event-change-location.component.html',
  styleUrl: './event-change-location.component.scss'
})
export class EventChangeLocationComponent implements OnInit {
  data = input<EventInfo>()
  hiddenFields = input<string[]>([])
  parent = input.required<FormGroup>()
  fg: FormGroup

  addressReadAPI = input.required<AddressReadAPI>()
  addressResource = resource({
    loader: (param) => {
      return toPromise(this.addressReadAPI().getAllAddresses(0, 100))
    }
  })
  addresses = computed(() => this.addressResource.value()?.content ?? [])
  loading = this.addressResource.isLoading
  error = this.addressResource.error

  private selectedAddressId = signal<number | undefined>(undefined)
  readonly selectedAddress = computed(() => this.addresses().find((a) => a.id === this.selectedAddressId()))

  constructor() {
    const fb = inject(FormBuilder)

    this.fg = fb.group({
      city: ['', Validators.required],
      country: ['Deutschland', Validators.required],
      street: ['', Validators.required],
      streetNumber: ['', Validators.required],
      zip: ['', Validators.required],
      additionalInfo: [''],
      addressMode: ['new'],
      saveAddress: [true]
    })

    effect(() => {
      const event = this.data()
      if (event) this.handleDataChanged(event)
    })

    effect(() => {
      const addresses = this.addresses()
      if (!this.data() && addresses.length > 0 && !this.addressMode.dirty) {
        this.addressMode.setValue('saved')
        const preferred = addresses.find((a) => a.standard) ?? addresses[0]
        this.selectAddress(preferred)
      }
    })

    effect(() => {
      const parent = this.parent()
      parent.addControl('location', this.fg)
    })
  }

  get addressMode(): FormControl {
    return this.fg.get('addressMode') as FormControl
  }

  ngOnInit() {
    this.addressResource.reload()
  }

  handleAddressSelected(event: MatRadioChange) {
    this.selectAddress(event.value as Address)
  }

  isVisible(ctrl: string): boolean {
    return this.hiddenFields().find((x) => x == ctrl) == null
  }

  private selectAddress(address: Address) {
    this.selectedAddressId.set(address.id)
    this.fg.patchValue({
      street: address.street,
      streetNumber: address.streetNumber,
      zip: address.zip,
      city: address.city,
      country: address.country,
      additionalInfo: address.additionalInfo
    })
  }

  private handleDataChanged(info: EventInfo) {
    const location = info.location
    if (location) {
      this.fg.patchValue({
        city: location.city ?? '',
        country: location.country ?? '',
        street: location.street ?? '',
        streetNumber: location.streetNumber ?? '',
        zip: location.zip ?? '',
        additionalInfo: location.additionalInfo ?? '',
        addressMode: 'new',
        saveAddress: false
      })
    }
  }
}

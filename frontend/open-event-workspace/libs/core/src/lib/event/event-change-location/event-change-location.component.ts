import {Component, computed, effect, input, OnInit, resource} from '@angular/core';
import {EventInfo} from "../event.api";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {CommonModule} from "@angular/common";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatInputModule} from "@angular/material/input";
import {TranslatePipe} from "@ngx-translate/core";
import {MatDividerModule} from "@angular/material/divider";
import {MatSelectChange, MatSelectModule} from "@angular/material/select";
import {Address, AddressReadAPI} from "../../address/address.api";
import {toPromise} from "@open-event-workspace/shared";

@Component({
  selector: 'lib-event-change-location',
  imports: [CommonModule, ReactiveFormsModule, MatFormFieldModule, MatDatepickerModule, MatInputModule, MatDividerModule, MatSelectModule, TranslatePipe],
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

  constructor(fb: FormBuilder) {
    this.fg = fb.group({
      city: ['', Validators.required],
      country: ['Deutschland', Validators.required],
      street: ['', Validators.required],
      streetNumber: ['', Validators.required],
      zip: ['', Validators.required],
      additionalInfo: ['']
    })

    effect(() => {
      let event = this.data()
      if (event) this.handleDataChanged(event)
    })

    effect(() => {
      let addresses = this.addresses()
      if (addresses.length > 0 && !this.fg?.dirty && !this.fg?.valid) {
        this.setAddress(addresses[0])
      }
    });

    effect(() => {
      let parent = this.parent()
      parent.addControl("location", this.fg)
    });
  }

  ngOnInit() {
    this.addressResource.reload()
  }

  handleAddressSelected(event: MatSelectChange) {
    let address = event.value as Address
    this.setAddress(address)
  }

  private setAddress(address: Address) {
    if (!this.fg) return
    this.fg.setValue(
      {
        'street': address.street,
        'streetNumber': address.streetNumber,
        'zip': address.zip,
        'city': address.city,
        'country': address.country,
        'additionalInfo': address.additionalInfo,
      }
    )

  }

  private handleDataChanged(info: EventInfo) {
    let location = info.location
    if (location) {
      this.fg.setValue({
        city: location.city ?? "",
        country: location.country ?? "",
        street: location.street ?? "",
        streetNumber: location.streetNumber ?? "",
        zip: location.zip ?? "",
        additionalInfo: location.additionalInfo ?? "",
      })
    }
  }

  isVisible(ctrl: string): boolean {
    return this.hiddenFields().find(x => x == ctrl) == null
  }
}

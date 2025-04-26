import {Component, effect, input, output} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {Address, AddressChangeRequest} from "../address.api";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'lib-address-change',
  imports: [
    FormsModule,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule,
    TranslatePipe
  ],
  templateUrl: './address-change.component.html',
  styleUrl: './address-change.component.scss'
})
export class AddressChangeComponent {

  data = input<Address>()
  request = output<AddressChangeRequest>()

  fg: FormGroup

  constructor(fb: FormBuilder) {
    this.fg = fb.group({
      street: ['', Validators.required],
      streetNumber: ['', Validators.required],
      zip: ['', Validators.required],
      city: ['', Validators.required],
      country: ['Deutschland', Validators.required],
      additionalInfo: [''],
      lat: [''],
      lon: [''],
    });


    effect(() => {
      let address = this.data()
      if (address) this.handleDataChanged(address)
    });
  }


  private handleDataChanged(address: Address) {
    this.fg.get('street')?.setValue(address.street)
    this.fg.get('streetNumber')?.setValue(address.streetNumber)
    this.fg.get('zip')?.setValue(address.zip)
    this.fg.get('city')?.setValue(address.city)
    this.fg.get('country')?.setValue(address.country)
    this.fg.get('additionalInfo')?.setValue(address.additionalInfo)
    this.fg.get('lat')?.setValue(address.lat)
    this.fg.get('lon')?.setValue(address.lon)
  }

  submit() {
    if (!this.fg.valid) return
    let value = this.fg.value
    let request = value as AddressChangeRequest
    this.request.emit(request)
  }
}

import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {ProfileRoutingModule} from './profile-routing.module';
import {MaterialModule} from "../material/material.module";
import {TranslateModule} from "@ngx-translate/core";
import {ReactiveFormsModule} from "@angular/forms";
import {AddressModule} from "../address/address.module";
import {PreferencesModule} from "../preferences/preferences.module";


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    MaterialModule,
    TranslateModule,
    ReactiveFormsModule,
    ProfileRoutingModule,
    AddressModule,
    PreferencesModule,
  ]
})
export class ProfileModule {
}

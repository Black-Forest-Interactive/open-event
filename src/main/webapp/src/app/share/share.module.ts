import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {ShareRoutingModule} from './share-routing.module';
import {ShareDetailsComponent} from './share-details/share-details.component';
import {AccountModule} from "../account/account.module";
import {ShareButtons} from "ngx-sharebuttons/buttons";
import {RegistrationModule} from "../registration/registration.module";
import {TranslateModule} from "@ngx-translate/core";
import {ShareInfoComponent} from './share-info/share-info.component';
import {LocationModule} from "../location/location.module";
import {MaterialModule} from "../material/material.module";
import {ReactiveFormsModule} from "@angular/forms";


@NgModule({
  declarations: [
    ShareDetailsComponent,
    ShareInfoComponent
  ],
  exports: [
    ShareDetailsComponent
  ],
  imports: [
    CommonModule,
    ShareRoutingModule,
    AccountModule,
    MaterialModule,
    TranslateModule,
    ReactiveFormsModule,
    ShareButtons,
    RegistrationModule,
    TranslateModule,
    LocationModule,
  ]
})
export class ShareModule {
}

import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LocationMapComponent} from './location-map/location-map.component';


@NgModule({
  declarations: [
    LocationMapComponent
  ],
  exports: [
    LocationMapComponent
  ],
  imports: [
    CommonModule
  ]
})
export class LocationModule {
}

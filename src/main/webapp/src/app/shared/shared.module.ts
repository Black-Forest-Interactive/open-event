import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ChipSelectModule} from "./chip-select-pane/chip-select.module";


@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ],
  exports: [
    ChipSelectModule,
  ]
})
export class SharedModule {
}

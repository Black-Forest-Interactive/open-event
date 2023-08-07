import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ChipSelectPaneComponent} from "./chip-select-pane.component";
import {MaterialModule} from "../../material/material.module";
import {ReactiveFormsModule} from "@angular/forms";

@NgModule({
  declarations: [ChipSelectPaneComponent],
  exports: [
    ChipSelectPaneComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    ReactiveFormsModule
  ]
})
export class ChipSelectModule {
}

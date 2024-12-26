import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {PreferencesBoardComponent} from './preferences-board/preferences-board.component';
import {MatCard} from "@angular/material/card";


@NgModule({
  declarations: [
    PreferencesBoardComponent
  ],
  exports: [
    PreferencesBoardComponent
  ],
  imports: [
    CommonModule,
    MatCard
  ]
})
export class PreferencesModule {
}

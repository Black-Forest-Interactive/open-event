import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {EventRoutingModule} from './event-routing.module';
import {MaterialModule} from "../material/material.module";
import {EventBoardComponent} from './event-board/event-board.component';
import {EventBoardHeaderComponent} from './event-board-header/event-board-header.component';
import {TranslateModule} from "@ngx-translate/core";
import {EventChangeComponent} from './event-change/event-change.component';
import {EventDetailsComponent} from './event-details/event-details.component';
import {ReactiveFormsModule} from "@angular/forms";
import {EventChangeFormEventComponent} from './event-change-form-event/event-change-form-event.component';
import {EventChangeFormLocationComponent} from './event-change-form-location/event-change-form-location.component';
import {
  EventChangeFormRegistrationComponent
} from './event-change-form-registration/event-change-form-registration.component';
import {HotToastModule} from "@ngneat/hot-toast";
import {EventMenuComponent} from './event-menu/event-menu.component';
import {EventDeleteDialogComponent} from './event-delete-dialog/event-delete-dialog.component';
import {AccountModule} from "../account/account.module";
import {LocationModule} from "../location/location.module";
import {EventBoardListComponent} from './event-board-list/event-board-list.component';
import {EventBoardListEntryComponent} from './event-board-list-entry/event-board-list-entry.component';
import {ChipSelectModule} from "../shared/chip-select-pane/chip-select.module";
import {EventBoardTableComponent} from './event-board-table/event-board-table.component';
import {EventDetailsHeaderComponent} from './event-details-header/event-details-header.component';
import {RegistrationModule} from "../registration/registration.module";
import {QuillModule} from "ngx-quill";


@NgModule({
  declarations: [
    EventBoardComponent,
    EventBoardHeaderComponent,
    EventChangeComponent,
    EventDetailsComponent,
    EventChangeFormEventComponent,
    EventChangeFormLocationComponent,
    EventChangeFormRegistrationComponent,
    EventMenuComponent,
    EventDeleteDialogComponent,
    EventBoardListComponent,
    EventBoardListEntryComponent,
    EventBoardTableComponent,
    EventDetailsHeaderComponent
  ],
  imports: [
    CommonModule,
    EventRoutingModule,
    MaterialModule,
    TranslateModule,
    ReactiveFormsModule,
    HotToastModule,
    AccountModule,
    LocationModule,
    ChipSelectModule,
    RegistrationModule,
    QuillModule
  ]
})
export class EventModule {
}

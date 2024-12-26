import {NgModule} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {EventRoutingModule} from './event-routing.module';
import {MaterialModule} from "../material/material.module";
import {EventBoardComponent} from './event-board/event-board.component';
import {EventBoardHeaderComponent} from './event-board-header/event-board-header.component';
import {TranslateModule} from "@ngx-translate/core";
import {EventChangeComponent} from './event-change/event-change.component';
import {EventDetailsComponent} from './event-details/event-details.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {EventChangeFormEventComponent} from './event-change-form-event/event-change-form-event.component';
import {EventChangeFormLocationComponent} from './event-change-form-location/event-change-form-location.component';
import {
  EventChangeFormRegistrationComponent
} from './event-change-form-registration/event-change-form-registration.component';
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
import {EventChangeHelpComponent} from './event-change-help/event-change-help.component';
import {EventBoardMapComponent} from './event-board-map/event-board-map.component';
import {EventBoardCalendarComponent} from './event-board-calendar/event-board-calendar.component';
import {EventBoardMapPopupComponent} from './event-board-map-popup/event-board-map-popup.component';
import {FullCalendarModule} from "@fullcalendar/angular";
import {EventActionExportComponent} from './event-action-export/event-action-export.component';
import {EventBoardFilterComponent} from "./event-board-filter/event-board-filter.component";
import {EventAdminComponent} from './event-admin/event-admin.component';
import {EventDetailsInfoComponent} from './event-details-info/event-details-info.component';
import {EventDetailsLocationComponent} from './event-details-location/event-details-location.component';
import {ShareModule} from "../share/share.module";
import {EventCreateDialogComponent} from './event-create-dialog/event-create-dialog.component';
import {ShareButtons} from "ngx-sharebuttons/buttons";
import {ScrollNearEndDirective} from "../shared/scroll-near-end.directive";


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
    EventDetailsHeaderComponent,
    EventChangeHelpComponent,
    EventBoardMapComponent,
    EventBoardCalendarComponent,
    EventBoardMapPopupComponent,
    EventActionExportComponent,
    EventBoardFilterComponent,
    EventAdminComponent,
    EventDetailsInfoComponent,
    EventDetailsLocationComponent,
    EventCreateDialogComponent
  ],
  exports: [
    EventChangeFormEventComponent,
    EventChangeFormLocationComponent,
    EventChangeFormRegistrationComponent,
    EventActionExportComponent
  ],
  imports: [
    CommonModule,
    EventRoutingModule,
    MaterialModule,
    TranslateModule,
    ReactiveFormsModule,
    AccountModule,
    LocationModule,
    ChipSelectModule,
    RegistrationModule,
    QuillModule,
    NgOptimizedImage,
    FullCalendarModule,
    FormsModule,
    ShareButtons,
    ShareModule,
    ScrollNearEndDirective,
  ]
})
export class EventModule {
}

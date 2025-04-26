import {AfterViewInit, Component, effect, ViewChild} from '@angular/core';
import {CalendarApi, CalendarOptions, EventClickArg} from "@fullcalendar/core";
import dayGridPlugin from '@fullcalendar/daygrid';
import {FullCalendarComponent, FullCalendarModule} from '@fullcalendar/angular';
import {EventNavigationService} from "../event-navigation.service";
import {Router} from "@angular/router";
import {EventBoardService} from "../event-board.service";
import {MatCard} from "@angular/material/card";
import {LoadingBarComponent} from "@open-event-workspace/shared";

@Component({
  selector: 'app-event-board-calendar',
  templateUrl: './event-board-calendar.component.html',
  styleUrls: ['./event-board-calendar.component.scss'],
  imports: [
    MatCard,
    FullCalendarModule,
    LoadingBarComponent
  ],
  standalone: true
})
export class EventBoardCalendarComponent implements AfterViewInit {

  @ViewChild(FullCalendarComponent) calendarComponent: FullCalendarComponent | undefined
  calendarOptions: CalendarOptions = {
    headerToolbar: {
      left: 'prev,next',
      center: 'title',
      right: ''
      // right: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek'
    },
    initialView: 'dayGridMonth',
    plugins: [dayGridPlugin],
    weekends: true,
    editable: true,
    selectable: true,
    selectMirror: true,
    dayMaxEvents: true,
    eventTimeFormat: { // like '14:30:00'
      hour: '2-digit',
      minute: '2-digit',
      meridiem: false,
      hour12: false
    },
    locale: 'de',
    nowIndicator: true,
    eventClick: this.handleEventClick.bind(this)
  }

  private calendarApi: CalendarApi | undefined

  constructor(
    public service: EventBoardService,
    private router: Router,
  ) {
    effect(() => {
      if (this.service.reloading()) this.updateCalendar()
    });
  }

  ngAfterViewInit() {
    if (this.calendarComponent) {
      this.calendarApi = this.calendarComponent.getApi()
      this.updateCalendar()
    }
  }

  handleEventClick(arg: EventClickArg) {
    let id = arg.event.id
    if (id) EventNavigationService.navigateToEventDetails(this.router, +id)
  }

  private updateCalendar() {
    if (!this.calendarApi) return

    this.calendarApi.removeAllEvents()
    this.service.entries.forEach(e => {
      this.calendarApi?.addEvent({
        id: e.id,
        title: e.title,
        start: e.start,
        end: e.finish
      })
    })
  }
}

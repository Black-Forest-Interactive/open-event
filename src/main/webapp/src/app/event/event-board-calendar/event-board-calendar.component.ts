import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {EventBoardService} from "../model/event-board.service";
import {CalendarApi, CalendarOptions, EventClickArg} from "@fullcalendar/core";
import dayGridPlugin from '@fullcalendar/daygrid';
import {Subscription} from "rxjs";
import {FullCalendarComponent} from '@fullcalendar/angular';
import {EventNavigationService} from "../event-navigation.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-event-board-calendar',
    templateUrl: './event-board-calendar.component.html',
    styleUrls: ['./event-board-calendar.component.scss'],
    standalone: false
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

  private subscription: Subscription | undefined
  private calendarApi: CalendarApi | undefined

  constructor(
    public service: EventBoardService,
    private router: Router,
  ) {
  }

  ngOnInit() {
    this.subscription = this.service.reloading.subscribe(_ => this.updateCalendar())
  }

  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe()
      this.subscription = undefined
    }
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

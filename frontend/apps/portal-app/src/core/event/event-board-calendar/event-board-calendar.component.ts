import { AfterViewInit, Component, effect, inject, ViewChild } from '@angular/core'
import { CalendarApi, CalendarOptions, EventClickArg } from '@fullcalendar/core'
import dayGridPlugin from '@fullcalendar/daygrid'
import { FullCalendarComponent, FullCalendarModule } from '@fullcalendar/angular'
import { EventNavigationService } from '../event-navigation.service'
import { Router } from '@angular/router'
import { EventBoardService } from '../event-board.service'
import { MatCard } from '@angular/material/card'
import { LoadingBarComponent } from '@open-event/shared'

@Component({
  selector: 'portal-event-board-calendar',
  templateUrl: './event-board-calendar.component.html',
  styleUrl: './event-board-calendar.component.scss',
  imports: [MatCard, FullCalendarModule, LoadingBarComponent],
  standalone: true
})
export class EventBoardCalendarComponent implements AfterViewInit {
  private service = inject(EventBoardService)
  private router = inject(Router)

  @ViewChild(FullCalendarComponent) calendarComponent: FullCalendarComponent | undefined

  readonly reloading = this.service.reloading

  calendarOptions: CalendarOptions = {
    headerToolbar: { left: 'prev,next', center: 'title', right: '' },
    initialView: 'dayGridMonth',
    plugins: [dayGridPlugin],
    weekends: true,
    editable: true,
    selectable: true,
    selectMirror: true,
    dayMaxEvents: true,
    eventTimeFormat: { hour: '2-digit', minute: '2-digit', meridiem: false, hour12: false },
    locale: 'de',
    nowIndicator: true,
    eventClick: this.handleEventClick.bind(this)
  }

  private calendarApi: CalendarApi | undefined

  constructor() {
    effect(() => {
      const entries = this.service.entries()
      if (!this.calendarApi) return
      this.calendarApi.removeAllEvents()
      entries.forEach(e => this.calendarApi?.addEvent({ id: e.id + '', title: e.title, start: e.start, end: e.finish }))
    })
  }

  ngAfterViewInit() {
    if (this.calendarComponent) {
      this.calendarApi = this.calendarComponent.getApi()
      this.updateCalendar()
    }
  }

  handleEventClick(arg: EventClickArg) {
    const id = arg.event.id
    if (id) EventNavigationService.navigateToEventDetails(this.router, +id)
  }

  private updateCalendar() {
    if (!this.calendarApi) return
    this.calendarApi.removeAllEvents()
    this.service.entries().forEach(e => this.calendarApi?.addEvent({ id: e.id + '', title: e.title, start: e.start, end: e.finish }))
  }
}

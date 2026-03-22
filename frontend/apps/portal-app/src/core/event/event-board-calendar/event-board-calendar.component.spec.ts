import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventBoardCalendarComponent} from './event-board-calendar.component';

describe('EventBoardCalendarComponent', () => {
  let component: EventBoardCalendarComponent;
  let fixture: ComponentFixture<EventBoardCalendarComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EventBoardCalendarComponent]
    });
    fixture = TestBed.createComponent(EventBoardCalendarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

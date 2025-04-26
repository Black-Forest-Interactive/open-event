import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventDetailsHistoryComponent} from './event-details-history.component';

describe('EventDetailsHistoryComponent', () => {
  let component: EventDetailsHistoryComponent;
  let fixture: ComponentFixture<EventDetailsHistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventDetailsHistoryComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventDetailsHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

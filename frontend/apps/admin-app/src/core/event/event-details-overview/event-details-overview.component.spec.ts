import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventDetailsOverviewComponent} from './event-details-overview.component';

describe('EventDetailsOverviewComponent', () => {
  let component: EventDetailsOverviewComponent;
  let fixture: ComponentFixture<EventDetailsOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventDetailsOverviewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventDetailsOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

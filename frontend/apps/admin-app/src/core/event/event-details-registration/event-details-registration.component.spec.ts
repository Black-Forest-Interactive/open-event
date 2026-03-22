import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventDetailsRegistrationComponent} from './event-details-registration.component';

describe('EventDetailsRegistrationComponent', () => {
  let component: EventDetailsRegistrationComponent;
  let fixture: ComponentFixture<EventDetailsRegistrationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventDetailsRegistrationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventDetailsRegistrationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

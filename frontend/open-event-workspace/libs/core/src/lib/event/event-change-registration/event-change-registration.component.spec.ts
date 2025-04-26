import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventChangeRegistrationComponent} from './event-change-registration.component';

describe('EventChangeRegistrationComponent', () => {
  let component: EventChangeRegistrationComponent;
  let fixture: ComponentFixture<EventChangeRegistrationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventChangeRegistrationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventChangeRegistrationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

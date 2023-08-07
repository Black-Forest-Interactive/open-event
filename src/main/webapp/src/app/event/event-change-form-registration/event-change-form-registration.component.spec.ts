import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventChangeFormRegistrationComponent} from './event-change-form-registration.component';

describe('EventChangeFormRegistrationComponent', () => {
  let component: EventChangeFormRegistrationComponent;
  let fixture: ComponentFixture<EventChangeFormRegistrationComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EventChangeFormRegistrationComponent]
    });
    fixture = TestBed.createComponent(EventChangeFormRegistrationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

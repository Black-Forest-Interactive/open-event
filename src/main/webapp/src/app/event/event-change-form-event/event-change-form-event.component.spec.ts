import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventChangeFormEventComponent} from './event-change-form-event.component';

describe('EventChangeFormEventComponent', () => {
  let component: EventChangeFormEventComponent;
  let fixture: ComponentFixture<EventChangeFormEventComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EventChangeFormEventComponent]
    });
    fixture = TestBed.createComponent(EventChangeFormEventComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

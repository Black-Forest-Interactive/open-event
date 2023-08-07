import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventChangeFormLocationComponent} from './event-change-form-location.component';

describe('EventChangeFormLocationComponent', () => {
  let component: EventChangeFormLocationComponent;
  let fixture: ComponentFixture<EventChangeFormLocationComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EventChangeFormLocationComponent]
    });
    fixture = TestBed.createComponent(EventChangeFormLocationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

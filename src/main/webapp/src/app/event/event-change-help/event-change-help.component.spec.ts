import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventChangeHelpComponent} from './event-change-help.component';

describe('EventChangeHelpComponent', () => {
  let component: EventChangeHelpComponent;
  let fixture: ComponentFixture<EventChangeHelpComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EventChangeHelpComponent]
    });
    fixture = TestBed.createComponent(EventChangeHelpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

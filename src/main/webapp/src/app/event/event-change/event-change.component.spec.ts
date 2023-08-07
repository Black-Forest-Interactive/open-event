import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventChangeComponent} from './event-change.component';

describe('EventChangeComponent', () => {
  let component: EventChangeComponent;
  let fixture: ComponentFixture<EventChangeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EventChangeComponent]
    });
    fixture = TestBed.createComponent(EventChangeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

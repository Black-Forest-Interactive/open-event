import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventDetailsHeaderComponent} from './event-details-header.component';

describe('EventDetailsHeaderComponent', () => {
  let component: EventDetailsHeaderComponent;
  let fixture: ComponentFixture<EventDetailsHeaderComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EventDetailsHeaderComponent]
    });
    fixture = TestBed.createComponent(EventDetailsHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventDetailsLocationComponent} from './event-details-location.component';

describe('EventDetailsLocationComponent', () => {
  let component: EventDetailsLocationComponent;
  let fixture: ComponentFixture<EventDetailsLocationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventDetailsLocationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventDetailsLocationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventChangeLocationComponent} from './event-change-location.component';

describe('EventChangeLocationComponent', () => {
  let component: EventChangeLocationComponent;
  let fixture: ComponentFixture<EventChangeLocationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventChangeLocationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventChangeLocationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

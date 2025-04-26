import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventRangePickerComponent} from './event-range-picker.component';

describe('EventRangePickerComponent', () => {
  let component: EventRangePickerComponent;
  let fixture: ComponentFixture<EventRangePickerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventRangePickerComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventRangePickerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

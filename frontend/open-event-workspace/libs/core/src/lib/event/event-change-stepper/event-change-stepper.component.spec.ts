import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventChangeStepperComponent} from './event-change-stepper.component';

describe('EventChangeStepperComponent', () => {
  let component: EventChangeStepperComponent;
  let fixture: ComponentFixture<EventChangeStepperComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventChangeStepperComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventChangeStepperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventChangeSingleComponent} from './event-change-single.component';

describe('EventChangeSingleComponent', () => {
  let component: EventChangeSingleComponent;
  let fixture: ComponentFixture<EventChangeSingleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventChangeSingleComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventChangeSingleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

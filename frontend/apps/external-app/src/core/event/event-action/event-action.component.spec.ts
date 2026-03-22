import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventActionComponent} from './event-action.component';

describe('EventActionComponent', () => {
  let component: EventActionComponent;
  let fixture: ComponentFixture<EventActionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventActionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventActionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

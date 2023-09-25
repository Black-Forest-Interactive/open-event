import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventCreateComponent} from './event-create.component';

describe('EventCreateComponent', () => {
  let component: EventCreateComponent;
  let fixture: ComponentFixture<EventCreateComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EventCreateComponent]
    });
    fixture = TestBed.createComponent(EventCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

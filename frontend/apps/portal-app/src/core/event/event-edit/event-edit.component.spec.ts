import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventEditComponent} from './event-edit.component';

describe('EventEditComponent', () => {
  let component: EventEditComponent;
  let fixture: ComponentFixture<EventEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventEditComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

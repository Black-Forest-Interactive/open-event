import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventCreateDialogComponent} from './event-create-dialog.component';

describe('EventCreateDialogComponent', () => {
  let component: EventCreateDialogComponent;
  let fixture: ComponentFixture<EventCreateDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventCreateDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventCreateDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

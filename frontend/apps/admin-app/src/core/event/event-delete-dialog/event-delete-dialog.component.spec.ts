import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventDeleteDialogComponent} from './event-delete-dialog.component';

describe('EventDeleteDialogComponent', () => {
  let component: EventDeleteDialogComponent;
  let fixture: ComponentFixture<EventDeleteDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventDeleteDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventDeleteDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

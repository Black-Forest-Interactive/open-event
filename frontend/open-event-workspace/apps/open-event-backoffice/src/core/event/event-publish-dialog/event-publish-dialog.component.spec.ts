import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventPublishDialogComponent} from './event-publish-dialog.component';

describe('EventPublishDialogComponent', () => {
  let component: EventPublishDialogComponent;
  let fixture: ComponentFixture<EventPublishDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventPublishDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventPublishDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

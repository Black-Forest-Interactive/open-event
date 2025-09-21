import {ComponentFixture, TestBed} from '@angular/core/testing';

import {FeedbackDetailsDialogComponent} from './feedback-details-dialog.component';

describe('FeedbackDetailsDialogComponent', () => {
  let component: FeedbackDetailsDialogComponent;
  let fixture: ComponentFixture<FeedbackDetailsDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FeedbackDetailsDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FeedbackDetailsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ActivityCleanupDialogComponent} from './activity-cleanup-dialog.component';

describe('ActivityCleanupDialogComponent', () => {
  let component: ActivityCleanupDialogComponent;
  let fixture: ComponentFixture<ActivityCleanupDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActivityCleanupDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActivityCleanupDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

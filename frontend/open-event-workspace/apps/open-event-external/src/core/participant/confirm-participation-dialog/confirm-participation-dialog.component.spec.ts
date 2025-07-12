import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ConfirmParticipationDialogComponent} from './confirm-participation-dialog.component';

describe('ConfirmParticipationDialogComponent', () => {
  let component: ConfirmParticipationDialogComponent;
  let fixture: ComponentFixture<ConfirmParticipationDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfirmParticipationDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfirmParticipationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

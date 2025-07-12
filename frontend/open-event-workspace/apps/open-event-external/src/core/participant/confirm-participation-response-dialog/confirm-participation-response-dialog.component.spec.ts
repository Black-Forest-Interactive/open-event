import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ConfirmParticipationResponseDialogComponent} from './confirm-participation-response-dialog.component';

describe('ConfirmParticipationResponseDialogComponent', () => {
  let component: ConfirmParticipationResponseDialogComponent;
  let fixture: ComponentFixture<ConfirmParticipationResponseDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfirmParticipationResponseDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfirmParticipationResponseDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

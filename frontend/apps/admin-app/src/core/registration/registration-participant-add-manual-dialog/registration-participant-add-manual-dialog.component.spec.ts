import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RegistrationParticipantAddManualDialogComponent} from './registration-participant-add-manual-dialog.component';

describe('RegistrationParticipantAddManualDialogComponent', () => {
  let component: RegistrationParticipantAddManualDialogComponent;
  let fixture: ComponentFixture<RegistrationParticipantAddManualDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistrationParticipantAddManualDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegistrationParticipantAddManualDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

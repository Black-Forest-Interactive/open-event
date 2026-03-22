import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RegistrationParticipantAddAccountDialogComponent} from './registration-participant-add-account-dialog.component';

describe('RegistrationParticipantAddAccountDialogComponent', () => {
  let component: RegistrationParticipantAddAccountDialogComponent;
  let fixture: ComponentFixture<RegistrationParticipantAddAccountDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistrationParticipantAddAccountDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegistrationParticipantAddAccountDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

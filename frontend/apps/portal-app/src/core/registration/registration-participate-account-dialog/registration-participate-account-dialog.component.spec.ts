import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RegistrationParticipateAccountDialogComponent} from './registration-participate-account-dialog.component';

describe('RegistrationParticipateAccountDialogComponent', () => {
  let component: RegistrationParticipateAccountDialogComponent;
  let fixture: ComponentFixture<RegistrationParticipateAccountDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegistrationParticipateAccountDialogComponent]
    });
    fixture = TestBed.createComponent(RegistrationParticipateAccountDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

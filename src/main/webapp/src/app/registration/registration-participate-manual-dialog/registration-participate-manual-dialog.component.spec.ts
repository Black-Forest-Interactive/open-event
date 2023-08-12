import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RegistrationParticipateManualDialogComponent} from './registration-participate-manual-dialog.component';

describe('RegistrationParticipateManualDialogComponent', () => {
  let component: RegistrationParticipateManualDialogComponent;
  let fixture: ComponentFixture<RegistrationParticipateManualDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegistrationParticipateManualDialogComponent]
    });
    fixture = TestBed.createComponent(RegistrationParticipateManualDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

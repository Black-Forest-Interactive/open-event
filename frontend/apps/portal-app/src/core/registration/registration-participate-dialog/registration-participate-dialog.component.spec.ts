import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RegistrationParticipateDialogComponent} from './registration-participate-dialog.component';

describe('RegistrationParticipateDialogComponent', () => {
  let component: RegistrationParticipateDialogComponent;
  let fixture: ComponentFixture<RegistrationParticipateDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegistrationParticipateDialogComponent]
    });
    fixture = TestBed.createComponent(RegistrationParticipateDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

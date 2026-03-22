import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RegistrationParticipantRemoveDialogComponent} from './registration-participant-remove-dialog.component';

describe('RegistrationParticipantRemoveDialogComponent', () => {
  let component: RegistrationParticipantRemoveDialogComponent;
  let fixture: ComponentFixture<RegistrationParticipantRemoveDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistrationParticipantRemoveDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegistrationParticipantRemoveDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RegistrationParticipantEditDialogComponent} from './registration-participant-edit-dialog.component';

describe('RegistrationParticipantEditDialogComponent', () => {
  let component: RegistrationParticipantEditDialogComponent;
  let fixture: ComponentFixture<RegistrationParticipantEditDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistrationParticipantEditDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegistrationParticipantEditDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

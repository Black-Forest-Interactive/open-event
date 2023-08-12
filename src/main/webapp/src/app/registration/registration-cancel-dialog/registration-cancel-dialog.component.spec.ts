import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RegistrationCancelDialogComponent} from './registration-cancel-dialog.component';

describe('RegistrationCancelDialogComponent', () => {
  let component: RegistrationCancelDialogComponent;
  let fixture: ComponentFixture<RegistrationCancelDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegistrationCancelDialogComponent]
    });
    fixture = TestBed.createComponent(RegistrationCancelDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

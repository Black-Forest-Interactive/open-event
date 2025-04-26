import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RegistrationEditDialogComponent} from './registration-edit-dialog.component';

describe('RegistrationEditDialogComponent', () => {
  let component: RegistrationEditDialogComponent;
  let fixture: ComponentFixture<RegistrationEditDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegistrationEditDialogComponent]
    });
    fixture = TestBed.createComponent(RegistrationEditDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RegistrationStatusComponent} from './registration-status.component';

describe('RegistrationStatusComponent', () => {
  let component: RegistrationStatusComponent;
  let fixture: ComponentFixture<RegistrationStatusComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegistrationStatusComponent]
    });
    fixture = TestBed.createComponent(RegistrationStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

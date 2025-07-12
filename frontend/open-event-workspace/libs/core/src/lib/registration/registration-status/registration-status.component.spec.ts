import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RegistrationStatusComponent} from './registration-status.component';

describe('RegistrationStatusComponent', () => {
  let component: RegistrationStatusComponent;
  let fixture: ComponentFixture<RegistrationStatusComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistrationStatusComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegistrationStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RegistrationTableComponent} from './registration-table.component';

describe('RegistrationTableComponent', () => {
  let component: RegistrationTableComponent;
  let fixture: ComponentFixture<RegistrationTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistrationTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegistrationTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

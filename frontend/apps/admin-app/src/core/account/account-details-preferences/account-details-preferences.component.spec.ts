import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AccountDetailsPreferencesComponent} from './account-details-preferences.component';

describe('AccountDetailsPreferencesComponent', () => {
  let component: AccountDetailsPreferencesComponent;
  let fixture: ComponentFixture<AccountDetailsPreferencesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccountDetailsPreferencesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AccountDetailsPreferencesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

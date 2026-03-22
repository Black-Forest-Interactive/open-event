import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AccountDetailsAddressComponent} from './account-details-address.component';

describe('AccountDetailsAddressComponent', () => {
  let component: AccountDetailsAddressComponent;
  let fixture: ComponentFixture<AccountDetailsAddressComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccountDetailsAddressComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AccountDetailsAddressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

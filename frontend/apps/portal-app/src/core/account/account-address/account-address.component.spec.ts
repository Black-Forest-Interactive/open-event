import {ComponentFixture, TestBed} from '@angular/core/testing';
import {AccountAddressComponent} from './account-address.component';

describe('AccountAddressComponent', () => {
  let component: AccountAddressComponent;
  let fixture: ComponentFixture<AccountAddressComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccountAddressComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(AccountAddressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

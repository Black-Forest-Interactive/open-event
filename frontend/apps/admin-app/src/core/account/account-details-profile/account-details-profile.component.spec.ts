import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AccountDetailsProfileComponent} from './account-details-profile.component';

describe('AccountDetailsProfileComponent', () => {
  let component: AccountDetailsProfileComponent;
  let fixture: ComponentFixture<AccountDetailsProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccountDetailsProfileComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AccountDetailsProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

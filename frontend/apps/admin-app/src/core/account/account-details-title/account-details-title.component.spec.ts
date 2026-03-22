import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AccountDetailsTitleComponent} from './account-details-title.component';

describe('AccountDetailsTitleComponent', () => {
  let component: AccountDetailsTitleComponent;
  let fixture: ComponentFixture<AccountDetailsTitleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccountDetailsTitleComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AccountDetailsTitleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AccountActivityComponent} from './account-activity.component';

describe('AccountActivityComponent', () => {
  let component: AccountActivityComponent;
  let fixture: ComponentFixture<AccountActivityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AccountActivityComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AccountActivityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

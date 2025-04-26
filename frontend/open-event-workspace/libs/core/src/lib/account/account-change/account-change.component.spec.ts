import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AccountChangeComponent} from './account-change.component';

describe('AccountChangeComponent', () => {
  let component: AccountChangeComponent;
  let fixture: ComponentFixture<AccountChangeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccountChangeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AccountChangeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

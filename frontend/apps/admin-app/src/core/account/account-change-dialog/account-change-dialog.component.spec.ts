import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AccountChangeDialogComponent} from './account-change-dialog.component';

describe('AccountChangeDialogComponent', () => {
  let component: AccountChangeDialogComponent;
  let fixture: ComponentFixture<AccountChangeDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccountChangeDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AccountChangeDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AccountBoardComponent} from './account-board.component';

describe('AccountBoardComponent', () => {
  let component: AccountBoardComponent;
  let fixture: ComponentFixture<AccountBoardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AccountBoardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AccountBoardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import {ComponentFixture, TestBed} from '@angular/core/testing';

import {MailBoardComponent} from './mail-board.component';

describe('MailBoardComponent', () => {
  let component: MailBoardComponent;
  let fixture: ComponentFixture<MailBoardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MailBoardComponent]
    });
    fixture = TestBed.createComponent(MailBoardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import {ComponentFixture, TestBed} from '@angular/core/testing';

import {HistoryBoardComponent} from './history-board.component';

describe('HistoryBoardComponent', () => {
  let component: HistoryBoardComponent;
  let fixture: ComponentFixture<HistoryBoardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HistoryBoardComponent]
    });
    fixture = TestBed.createComponent(HistoryBoardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

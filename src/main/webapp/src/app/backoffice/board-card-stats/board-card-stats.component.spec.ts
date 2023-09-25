import {ComponentFixture, TestBed} from '@angular/core/testing';

import {BoardCardStatsComponent} from './board-card-stats.component';

describe('BoardCardStatsComponent', () => {
  let component: BoardCardStatsComponent;
  let fixture: ComponentFixture<BoardCardStatsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BoardCardStatsComponent]
    });
    fixture = TestBed.createComponent(BoardCardStatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

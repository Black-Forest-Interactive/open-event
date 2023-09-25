import {ComponentFixture, TestBed} from '@angular/core/testing';

import {BoardCardAccountComponent} from './board-card-account.component';

describe('BoardCardAccountComponent', () => {
  let component: BoardCardAccountComponent;
  let fixture: ComponentFixture<BoardCardAccountComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BoardCardAccountComponent]
    });
    fixture = TestBed.createComponent(BoardCardAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

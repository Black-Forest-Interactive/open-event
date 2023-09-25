import {ComponentFixture, TestBed} from '@angular/core/testing';

import {BoardCardEventComponent} from './board-card-event.component';

describe('BoardCardEventComponent', () => {
  let component: BoardCardEventComponent;
  let fixture: ComponentFixture<BoardCardEventComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BoardCardEventComponent]
    });
    fixture = TestBed.createComponent(BoardCardEventComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

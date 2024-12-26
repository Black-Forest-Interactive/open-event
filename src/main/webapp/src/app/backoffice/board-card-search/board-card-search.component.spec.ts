import {ComponentFixture, TestBed} from '@angular/core/testing';

import {BoardCardSearchComponent} from './board-card-search.component';

describe('BoardCardSearchComponent', () => {
  let component: BoardCardSearchComponent;
  let fixture: ComponentFixture<BoardCardSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BoardCardSearchComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BoardCardSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

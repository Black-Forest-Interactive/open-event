import {ComponentFixture, TestBed} from '@angular/core/testing';

import {BoardToolbarSearchComponent} from './board-toolbar-search.component';

describe('BoardToolbarSearchComponent', () => {
  let component: BoardToolbarSearchComponent;
  let fixture: ComponentFixture<BoardToolbarSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BoardToolbarSearchComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BoardToolbarSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

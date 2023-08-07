import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CategoryBoardComponent} from './category-board.component';

describe('CategoryBoardComponent', () => {
  let component: CategoryBoardComponent;
  let fixture: ComponentFixture<CategoryBoardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CategoryBoardComponent]
    });
    fixture = TestBed.createComponent(CategoryBoardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

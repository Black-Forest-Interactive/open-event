import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CategoryChangeComponent} from './category-change.component';

describe('CategoryChangeComponent', () => {
  let component: CategoryChangeComponent;
  let fixture: ComponentFixture<CategoryChangeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CategoryChangeComponent]
    });
    fixture = TestBed.createComponent(CategoryChangeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

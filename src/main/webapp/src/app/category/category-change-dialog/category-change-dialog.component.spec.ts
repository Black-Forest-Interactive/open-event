import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CategoryChangeDialogComponent} from './category-change-dialog.component';

describe('CategoryChangeDialogComponent', () => {
  let component: CategoryChangeDialogComponent;
  let fixture: ComponentFixture<CategoryChangeDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CategoryChangeDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CategoryChangeDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

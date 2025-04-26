import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CategoryDeleteDialogComponent} from './category-delete-dialog.component';

describe('CategoryDeleteDialogComponent', () => {
  let component: CategoryDeleteDialogComponent;
  let fixture: ComponentFixture<CategoryDeleteDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CategoryDeleteDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CategoryDeleteDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

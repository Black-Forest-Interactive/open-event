import {ComponentFixture, TestBed} from '@angular/core/testing';

import {SearchAccountDialogComponent} from './search-account-dialog.component';

describe('SearchAccountDialogComponent', () => {
  let component: SearchAccountDialogComponent;
  let fixture: ComponentFixture<SearchAccountDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SearchAccountDialogComponent]
    });
    fixture = TestBed.createComponent(SearchAccountDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

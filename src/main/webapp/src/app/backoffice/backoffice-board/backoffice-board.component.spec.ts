import {ComponentFixture, TestBed} from '@angular/core/testing';

import {BackofficeBoardComponent} from './backoffice-board.component';

describe('BackofficeBoardComponent', () => {
  let component: BackofficeBoardComponent;
  let fixture: ComponentFixture<BackofficeBoardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BackofficeBoardComponent]
    });
    fixture = TestBed.createComponent(BackofficeBoardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

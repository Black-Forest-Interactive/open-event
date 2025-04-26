import {ComponentFixture, TestBed} from '@angular/core/testing';

import {PreferencesBoardComponent} from './preferences-board.component';

describe('PreferencesBoardComponent', () => {
  let component: PreferencesBoardComponent;
  let fixture: ComponentFixture<PreferencesBoardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PreferencesBoardComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(PreferencesBoardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

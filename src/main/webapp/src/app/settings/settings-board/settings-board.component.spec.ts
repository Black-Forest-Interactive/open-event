import {ComponentFixture, TestBed} from '@angular/core/testing';

import {SettingsBoardComponent} from './settings-board.component';

describe('SettingsBoardComponent', () => {
  let component: SettingsBoardComponent;
  let fixture: ComponentFixture<SettingsBoardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SettingsBoardComponent]
    });
    fixture = TestBed.createComponent(SettingsBoardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

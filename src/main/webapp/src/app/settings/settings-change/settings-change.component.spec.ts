import {ComponentFixture, TestBed} from '@angular/core/testing';

import {SettingsChangeComponent} from './settings-change.component';

describe('SettingsChangeComponent', () => {
  let component: SettingsChangeComponent;
  let fixture: ComponentFixture<SettingsChangeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SettingsChangeComponent]
    });
    fixture = TestBed.createComponent(SettingsChangeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

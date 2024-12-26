import {ComponentFixture, TestBed} from '@angular/core/testing';

import {SettingsChangeDialogComponent} from './settings-change-dialog.component';

describe('SettingsChangeDialogComponent', () => {
  let component: SettingsChangeDialogComponent;
  let fixture: ComponentFixture<SettingsChangeDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SettingsChangeDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SettingsChangeDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

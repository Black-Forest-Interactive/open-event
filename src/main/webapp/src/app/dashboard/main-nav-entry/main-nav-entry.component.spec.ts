import {ComponentFixture, TestBed} from '@angular/core/testing';

import {MainNavEntryComponent} from './main-nav-entry.component';

describe('MainNavEntryComponent', () => {
  let component: MainNavEntryComponent;
  let fixture: ComponentFixture<MainNavEntryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MainNavEntryComponent]
    });
    fixture = TestBed.createComponent(MainNavEntryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

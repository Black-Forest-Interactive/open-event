import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventActionExportComponent} from './event-action-export.component';

describe('EventActionExportComponent', () => {
  let component: EventActionExportComponent;
  let fixture: ComponentFixture<EventActionExportComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EventActionExportComponent]
    });
    fixture = TestBed.createComponent(EventActionExportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

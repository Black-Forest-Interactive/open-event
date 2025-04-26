import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventBoardListEntryComponent} from './event-board-list-entry.component';

describe('EventBoardListEntryComponent', () => {
  let component: EventBoardListEntryComponent;
  let fixture: ComponentFixture<EventBoardListEntryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EventBoardListEntryComponent]
    });
    fixture = TestBed.createComponent(EventBoardListEntryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

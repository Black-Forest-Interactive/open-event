import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventBoardTableComponent} from './event-board-table.component';

describe('EventBoardTableComponent', () => {
  let component: EventBoardTableComponent;
  let fixture: ComponentFixture<EventBoardTableComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EventBoardTableComponent]
    });
    fixture = TestBed.createComponent(EventBoardTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

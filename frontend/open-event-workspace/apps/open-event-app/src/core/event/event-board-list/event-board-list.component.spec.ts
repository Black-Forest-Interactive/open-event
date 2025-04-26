import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventBoardListComponent} from './event-board-list.component';

describe('EventBoardListComponent', () => {
  let component: EventBoardListComponent;
  let fixture: ComponentFixture<EventBoardListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EventBoardListComponent]
    });
    fixture = TestBed.createComponent(EventBoardListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

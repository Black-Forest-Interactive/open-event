import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventBoardHeaderComponent} from './event-board-header.component';

describe('EventBoardHeaderComponent', () => {
  let component: EventBoardHeaderComponent;
  let fixture: ComponentFixture<EventBoardHeaderComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EventBoardHeaderComponent]
    });
    fixture = TestBed.createComponent(EventBoardHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

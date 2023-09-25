import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventBoardMapComponent} from './event-board-map.component';

describe('EventBoardMapComponent', () => {
  let component: EventBoardMapComponent;
  let fixture: ComponentFixture<EventBoardMapComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EventBoardMapComponent]
    });
    fixture = TestBed.createComponent(EventBoardMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

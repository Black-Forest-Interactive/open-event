import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventBoardMapPopupComponent} from './event-board-map-popup.component';

describe('EventBoardMapPopupComponent', () => {
  let component: EventBoardMapPopupComponent;
  let fixture: ComponentFixture<EventBoardMapPopupComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EventBoardMapPopupComponent]
    });
    fixture = TestBed.createComponent(EventBoardMapPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

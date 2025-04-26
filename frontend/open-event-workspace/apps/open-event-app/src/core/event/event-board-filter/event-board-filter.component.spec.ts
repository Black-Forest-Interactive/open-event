import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventBoardFilterComponent} from './event-board-filter.component';

describe('EventBoardFilterComponent', () => {
  let component: EventBoardFilterComponent;
  let fixture: ComponentFixture<EventBoardFilterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventBoardFilterComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventBoardFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

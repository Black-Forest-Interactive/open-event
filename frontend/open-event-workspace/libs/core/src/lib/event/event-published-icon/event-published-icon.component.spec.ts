import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventPublishedIconComponent} from './event-published-icon.component';

describe('EventPublishedIconComponent', () => {
  let component: EventPublishedIconComponent;
  let fixture: ComponentFixture<EventPublishedIconComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventPublishedIconComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventPublishedIconComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

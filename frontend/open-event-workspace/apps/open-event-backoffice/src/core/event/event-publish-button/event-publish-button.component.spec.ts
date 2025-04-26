import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventPublishButtonComponent} from './event-publish-button.component';

describe('EventPublishButtonComponent', () => {
  let component: EventPublishButtonComponent;
  let fixture: ComponentFixture<EventPublishButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventPublishButtonComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventPublishButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventDetailsBannerComponent} from './event-details-banner.component';

describe('EventDetailsBannerComponent', () => {
  let component: EventDetailsBannerComponent;
  let fixture: ComponentFixture<EventDetailsBannerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventDetailsBannerComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventDetailsBannerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

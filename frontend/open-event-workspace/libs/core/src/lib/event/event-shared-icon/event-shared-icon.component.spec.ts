import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventSharedIconComponent} from './event-shared-icon.component';

describe('EventSharedIconComponent', () => {
  let component: EventSharedIconComponent;
  let fixture: ComponentFixture<EventSharedIconComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventSharedIconComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventSharedIconComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

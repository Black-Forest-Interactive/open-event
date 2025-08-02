import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventChangeUploadComponent} from './event-change-upload.component';

describe('EventChangeUploadComponent', () => {
  let component: EventChangeUploadComponent;
  let fixture: ComponentFixture<EventChangeUploadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventChangeUploadComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventChangeUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

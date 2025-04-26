import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventChangeGeneralComponent} from './event-change-general.component';

describe('EventChangeGeneralComponent', () => {
  let component: EventChangeGeneralComponent;
  let fixture: ComponentFixture<EventChangeGeneralComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventChangeGeneralComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventChangeGeneralComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

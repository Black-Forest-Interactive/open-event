import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EventAdminComponent} from './event-admin.component';

describe('EventAdminComponent', () => {
  let component: EventAdminComponent;
  let fixture: ComponentFixture<EventAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EventAdminComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

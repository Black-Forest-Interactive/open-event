import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActivityButtonComponent } from './activity-button.component';

describe('ActivityButtonComponent', () => {
  let component: ActivityButtonComponent;
  let fixture: ComponentFixture<ActivityButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ActivityButtonComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ActivityButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

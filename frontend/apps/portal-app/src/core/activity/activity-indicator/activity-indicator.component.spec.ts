import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ActivityIndicatorComponent} from './activity-indicator.component';

describe('ActivityIndicatorComponent', () => {
  let component: ActivityIndicatorComponent;
  let fixture: ComponentFixture<ActivityIndicatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActivityIndicatorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActivityIndicatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

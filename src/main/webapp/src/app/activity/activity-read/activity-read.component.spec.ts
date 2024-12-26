import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ActivityReadComponent} from './activity-read.component';

describe('ActivityReadComponent', () => {
  let component: ActivityReadComponent;
  let fixture: ComponentFixture<ActivityReadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ActivityReadComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActivityReadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

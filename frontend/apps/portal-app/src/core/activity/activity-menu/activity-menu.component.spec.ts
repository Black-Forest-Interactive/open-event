import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ActivityMenuComponent} from './activity-menu.component';

describe('ActivityMenuComponent', () => {
  let component: ActivityMenuComponent;
  let fixture: ComponentFixture<ActivityMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActivityMenuComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ActivityMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

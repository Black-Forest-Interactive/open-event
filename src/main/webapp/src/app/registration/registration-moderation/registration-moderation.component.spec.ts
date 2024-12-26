import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RegistrationModerationComponent} from './registration-moderation.component';

describe('RegistrationModerationComponent', () => {
  let component: RegistrationModerationComponent;
  let fixture: ComponentFixture<RegistrationModerationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegistrationModerationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegistrationModerationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

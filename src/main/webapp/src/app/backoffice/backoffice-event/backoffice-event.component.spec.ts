import {ComponentFixture, TestBed} from '@angular/core/testing';

import {BackofficeEventComponent} from './backoffice-event.component';

describe('BackofficeEventComponent', () => {
  let component: BackofficeEventComponent;
  let fixture: ComponentFixture<BackofficeEventComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BackofficeEventComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BackofficeEventComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

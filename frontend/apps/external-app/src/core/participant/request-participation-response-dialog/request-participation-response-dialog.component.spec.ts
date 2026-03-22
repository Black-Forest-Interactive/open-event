import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RequestParticipationResponseDialogComponent} from './request-participation-response-dialog.component';

describe('RequestParticipationResponseDialogComponent', () => {
  let component: RequestParticipationResponseDialogComponent;
  let fixture: ComponentFixture<RequestParticipationResponseDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RequestParticipationResponseDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RequestParticipationResponseDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

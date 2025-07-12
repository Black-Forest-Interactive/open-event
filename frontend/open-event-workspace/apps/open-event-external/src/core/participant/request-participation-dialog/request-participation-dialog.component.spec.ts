import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RequestParticipationDialogComponent} from './request-participation-dialog.component';

describe('RequestParticipationDialogComponent', () => {
  let component: RequestParticipationDialogComponent;
  let fixture: ComponentFixture<RequestParticipationDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RequestParticipationDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RequestParticipationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

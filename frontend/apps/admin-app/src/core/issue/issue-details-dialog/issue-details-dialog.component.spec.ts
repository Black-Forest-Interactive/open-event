import {ComponentFixture, TestBed} from '@angular/core/testing';

import {IssueDetailsDialogComponent} from './issue-details-dialog.component';

describe('IssueDetailsDialogComponent', () => {
  let component: IssueDetailsDialogComponent;
  let fixture: ComponentFixture<IssueDetailsDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IssueDetailsDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IssueDetailsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

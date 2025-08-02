import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExportSummaryButtonComponent } from './export-summary-button.component';

describe('ExportSummaryButtonComponent', () => {
  let component: ExportSummaryButtonComponent;
  let fixture: ComponentFixture<ExportSummaryButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExportSummaryButtonComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExportSummaryButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

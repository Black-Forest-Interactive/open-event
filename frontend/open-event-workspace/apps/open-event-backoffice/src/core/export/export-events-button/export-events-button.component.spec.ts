import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExportEventsButtonComponent } from './export-events-button.component';

describe('ExportEventsButtonComponent', () => {
  let component: ExportEventsButtonComponent;
  let fixture: ComponentFixture<ExportEventsButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExportEventsButtonComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExportEventsButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

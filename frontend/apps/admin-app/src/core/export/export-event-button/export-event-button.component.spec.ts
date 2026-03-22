import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ExportEventButtonComponent} from './export-event-button.component';

describe('ExportEventButtonComponent', () => {
  let component: ExportEventButtonComponent;
  let fixture: ComponentFixture<ExportEventButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExportEventButtonComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExportEventButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

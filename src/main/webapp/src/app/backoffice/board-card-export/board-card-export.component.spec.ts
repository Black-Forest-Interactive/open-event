import {ComponentFixture, TestBed} from '@angular/core/testing';

import {BoardCardExportComponent} from './board-card-export.component';

describe('BoardCardExportComponent', () => {
  let component: BoardCardExportComponent;
  let fixture: ComponentFixture<BoardCardExportComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BoardCardExportComponent]
    });
    fixture = TestBed.createComponent(BoardCardExportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

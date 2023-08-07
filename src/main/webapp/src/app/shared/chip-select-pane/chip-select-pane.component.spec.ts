import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ChipSelectPaneComponent} from './chip-select-pane.component';

describe('ChipSelectPaneComponent', () => {
  let component: ChipSelectPaneComponent;
  let fixture: ComponentFixture<ChipSelectPaneComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChipSelectPaneComponent]
    });
    fixture = TestBed.createComponent(ChipSelectPaneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

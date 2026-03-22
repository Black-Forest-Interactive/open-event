import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AddressBoardComponent} from './address-board.component';

describe('AddressBoardComponent', () => {
  let component: AddressBoardComponent;
  let fixture: ComponentFixture<AddressBoardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddressBoardComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AddressBoardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

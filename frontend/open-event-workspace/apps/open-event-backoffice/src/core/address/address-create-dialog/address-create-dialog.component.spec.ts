import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AddressCreateDialogComponent} from './address-create-dialog.component';

describe('AddressCreateDialogComponent', () => {
  let component: AddressCreateDialogComponent;
  let fixture: ComponentFixture<AddressCreateDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddressCreateDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddressCreateDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

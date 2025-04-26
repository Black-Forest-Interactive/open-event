import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AddressDeleteDialogComponent} from './address-delete-dialog.component';

describe('AddressDeleteDialogComponent', () => {
  let component: AddressDeleteDialogComponent;
  let fixture: ComponentFixture<AddressDeleteDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddressDeleteDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddressDeleteDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

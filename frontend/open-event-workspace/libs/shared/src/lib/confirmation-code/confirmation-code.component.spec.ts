import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ConfirmationCodeComponent} from './confirmation-code.component';

describe('ConfirmationCodeComponent', () => {
  let component: ConfirmationCodeComponent;
  let fixture: ComponentFixture<ConfirmationCodeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfirmationCodeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfirmationCodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

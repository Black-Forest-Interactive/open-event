import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CreateAccountDialogComponent} from './create-account-dialog.component';

describe('CreateAccountDialogComponent', () => {
  let component: CreateAccountDialogComponent;
  let fixture: ComponentFixture<CreateAccountDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreateAccountDialogComponent]
    });
    fixture = TestBed.createComponent(CreateAccountDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

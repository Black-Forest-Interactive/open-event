import {ComponentFixture, TestBed} from '@angular/core/testing';

import {BackofficeAdminComponent} from './backoffice-admin.component';

describe('BackofficeAdminComponent', () => {
  let component: BackofficeAdminComponent;
  let fixture: ComponentFixture<BackofficeAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BackofficeAdminComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BackofficeAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

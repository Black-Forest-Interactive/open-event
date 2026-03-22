import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ShareDetailsComponent} from './share-details.component';

describe('ShareDetailsComponent', () => {
  let component: ShareDetailsComponent;
  let fixture: ComponentFixture<ShareDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ShareDetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShareDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

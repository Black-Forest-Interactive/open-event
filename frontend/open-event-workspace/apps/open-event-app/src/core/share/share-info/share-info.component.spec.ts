import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ShareInfoComponent} from './share-info.component';

describe('ShareInfoComponent', () => {
  let component: ShareInfoComponent;
  let fixture: ComponentFixture<ShareInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ShareInfoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShareInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

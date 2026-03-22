import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AccountDetailsEventsComponent} from './account-details-events.component';

describe('AccountDetailsEventsComponent', () => {
  let component: AccountDetailsEventsComponent;
  let fixture: ComponentFixture<AccountDetailsEventsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccountDetailsEventsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AccountDetailsEventsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

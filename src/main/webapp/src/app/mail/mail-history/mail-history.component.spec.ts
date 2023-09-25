import {ComponentFixture, TestBed} from '@angular/core/testing';

import {MailHistoryComponent} from './mail-history.component';

describe('MailHistoryComponent', () => {
  let component: MailHistoryComponent;
  let fixture: ComponentFixture<MailHistoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MailHistoryComponent]
    });
    fixture = TestBed.createComponent(MailHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

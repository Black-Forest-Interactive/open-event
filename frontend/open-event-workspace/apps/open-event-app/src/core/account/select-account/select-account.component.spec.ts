import {ComponentFixture, TestBed} from '@angular/core/testing';
import {SelectAccountComponent} from './select-account.component';

describe('SelectAccountComponent', () => {
  let component: SelectAccountComponent;
  let fixture: ComponentFixture<SelectAccountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SelectAccountComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SelectAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import {ComponentFixture, TestBed} from '@angular/core/testing';
import {SearchAccountDialogComponent} from './search-account-dialog.component';

describe('SearchAccountDialogComponent', () => {
  let component: SearchAccountDialogComponent;
  let fixture: ComponentFixture<SearchAccountDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchAccountDialogComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SearchAccountDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

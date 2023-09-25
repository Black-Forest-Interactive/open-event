import {ComponentFixture, TestBed} from '@angular/core/testing';

import {BoardCardSolrComponent} from './board-card-solr.component';

describe('BoardCardSolrComponent', () => {
  let component: BoardCardSolrComponent;
  let fixture: ComponentFixture<BoardCardSolrComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BoardCardSolrComponent]
    });
    fixture = TestBed.createComponent(BoardCardSolrComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

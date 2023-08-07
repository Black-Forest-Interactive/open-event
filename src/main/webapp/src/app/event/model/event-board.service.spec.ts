import {TestBed} from '@angular/core/testing';

import {EventBoardService} from './event-board.service';

describe('EventBoardService', () => {
  let service: EventBoardService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EventBoardService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

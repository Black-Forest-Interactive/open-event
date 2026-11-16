import { Injectable } from '@angular/core'
import { HttpParams } from '@angular/common/http'
import { EventSearchEntry, Finder, FinderAnswer, FinderAnswerRequest, FinderChangeRequest, FinderEntry, FinderEntryInfo, FinderInfo, FinderPreview } from '@open-event/core'
import { BaseService, Page } from '@open-event/shared'
import { Observable } from 'rxjs'

@Injectable({
  providedIn: 'root'
})
export class FinderService extends BaseService {
  constructor() {
    super('portal/finder')
    this.retryCount = 1
  }

  getFinders(page: number, size: number): Observable<Page<FinderInfo>> {
    return this.getPaged('', page, size)
  }

  getFinder(id: number): Observable<FinderInfo> {
    return this.get('' + id)
  }

  createFinder(request: FinderChangeRequest): Observable<Finder> {
    return this.post('', request)
  }

  preview(request: FinderChangeRequest): Observable<FinderPreview> {
    return this.post('preview', request)
  }

  updateFinder(id: number, request: FinderChangeRequest): Observable<Finder> {
    return this.put('' + id, request)
  }

  deleteFinder(id: number): Observable<Finder> {
    return this.delete('' + id)
  }

  getOpen(id: number, page: number, size: number): Observable<Page<EventSearchEntry>> {
    return this.getPaged('' + id + '/open', page, size)
  }

  getEntries(id: number, answer: FinderAnswer, page: number, size: number): Observable<Page<FinderEntryInfo>> {
    return this.getPaged('' + id + '/entry', page, size, new HttpParams().set('answer', answer))
  }

  setAnswer(id: number, eventId: number, answer: FinderAnswer): Observable<FinderEntry> {
    return this.put('' + id + '/entry/' + eventId, new FinderAnswerRequest(answer))
  }

  clearAnswer(id: number, eventId: number): Observable<Finder> {
    return this.delete('' + id + '/entry/' + eventId)
  }

  resetAnswers(id: number): Observable<Finder> {
    return this.delete('' + id + '/entry')
  }
}

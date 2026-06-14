import { Injectable } from '@angular/core'
import { BaseService, Page } from '@open-event/shared'
import { Observable } from 'rxjs'
import { Category } from '@open-event/core'

@Injectable({
  providedIn: 'root'
})
export class CategoryService extends BaseService {
  constructor() {
    super('app/category')
    this.retryCount = 1
  }

  getCategories(page: number, size: number): Observable<Page<Category>> {
    return this.getPaged('', page, size)
  }

  getCategory(id: number): Observable<Category> {
    return this.get('' + id)
  }
}

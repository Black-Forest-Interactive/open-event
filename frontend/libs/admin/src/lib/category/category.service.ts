import { Injectable } from '@angular/core'
import { HttpParams } from '@angular/common/http'
import { Observable } from 'rxjs'
import { BaseService, Page } from '@open-event/shared'
import { Category, CategoryChangeRequest, CategoryReadAPI, CategorySearchRequest, CategorySearchResponse } from '@open-event/core'

@Injectable({
  providedIn: 'root'
})
export class CategoryService extends BaseService implements CategoryReadAPI {
  constructor() {
    super('backoffice/category')
    this.retryCount = 0
  }

  getCategory(id: number): Observable<Category> {
    return this.get('' + id)
  }

  getAllCategories(page: number, size: number): Observable<Page<Category>> {
    return this.getPaged('', page, size)
  }

  createCategory(request: CategoryChangeRequest): Observable<Category> {
    return this.post('', request)
  }

  updateCategory(id: number, request: CategoryChangeRequest): Observable<Category> {
    return this.put('' + id, request)
  }

  deleteCategory(id: number): Observable<Category> {
    return this.delete('' + id)
  }

  search(request: CategorySearchRequest, page: number, size: number): Observable<CategorySearchResponse> {
    const params = new HttpParams().set('page', page).set('size', size)
    return this.post('search', request, params)
  }
}

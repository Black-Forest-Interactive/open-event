import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Category, CategoryChangeRequest} from "./category-api";
import {Page} from "../../shared/model/page";
import {BaseService} from "../../shared/model/base-service";

@Injectable({
  providedIn: 'root'
})
export class CategoryService extends BaseService {
  constructor(http: HttpClient) {
    super(http, 'category')
    this.retryCount = 1
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
}

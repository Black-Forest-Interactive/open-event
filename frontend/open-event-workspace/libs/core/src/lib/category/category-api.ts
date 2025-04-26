import {Observable} from "rxjs";
import {Page} from "@open-event-workspace/shared";

export interface Category {
  id: number,
  name: string,
  iconUrl: string
}

export class CategoryChangeRequest {
  constructor(
    public name: string,
    public iconUrl: string
  ) {
  }
}

export interface CategoryReadAPI {
  getCategory(id: number): Observable<Category>

  getAllCategories(page: number, size: number): Observable<Page<Category>>
}

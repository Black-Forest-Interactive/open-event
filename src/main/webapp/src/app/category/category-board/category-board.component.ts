import {Component} from '@angular/core';
import {Category} from "../model/category-api";
import {CategoryService} from "../model/category.service";
import {Page} from "../../shared/model/page";

@Component({
  selector: 'app-category-board',
  templateUrl: './category-board.component.html',
  styleUrls: ['./category-board.component.scss']
})
export class CategoryBoardComponent {
  reloading: boolean = false
  pageSize: number = 20
  pageIndex: number = 0
  totalSize: number = 0
  categories: Category[] = []

  constructor(private service: CategoryService) {

  }

  ngOnInit() {
    this.reload()
  }

  private reload() {
    if (this.reloading) return
    this.reloading = true
    this.service.getAllCategories(this.pageIndex, this.pageSize).subscribe(p => this.handleData(p))
  }

  private handleData(p: Page<Category>) {
    this.categories = p.content
    this.pageSize = p.pageable.size
    this.pageIndex = p.pageable.number
    this.totalSize = p.totalSize
    this.reloading = false
  }
}

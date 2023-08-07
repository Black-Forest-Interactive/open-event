import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {TranslateService} from "@ngx-translate/core";
import {HotToastService} from "@ngneat/hot-toast";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import {Location} from "@angular/common";
import {CategoryService} from "../model/category.service";
import {Category, CategoryChangeRequest} from "../model/category-api";

@Component({
  selector: 'app-category-change',
  templateUrl: './category-change.component.html',
  styleUrls: ['./category-change.component.scss']
})
export class CategoryChangeComponent {

  title: string = "category.change.Create"
  reloading: boolean = false

  fg: FormGroup
  category: Category | undefined

  constructor(
    private fb: FormBuilder,
    private service: CategoryService,
    private translationService: TranslateService,
    private toastService: HotToastService,
    private router: Router,
    private route: ActivatedRoute,
    private location: Location
  ) {
    this.fg = this.fb.group({
      name: this.fb.control('', Validators.required),
      iconUrl: this.fb.control(''),
    })

  }

  ngOnInit() {
    this.route.paramMap.subscribe(p => this.handleParams(p))
  }

  private handleParams(p: ParamMap) {
    let idParam = p.get('id')
    let id = idParam !== null ? +idParam : null;

    if (id == null) {
      this.handleDataCreate()
    } else {
      this.loadData(id, (c) => this.handleDataEdit(c))
    }
  }

  private loadData(id: number, callback: (c: Category) => void) {
    this.reloading = true
    this.service.getCategory(id).subscribe(data => callback(data));
  }


  private handleDataCreate() {
    this.translationService.get("category.change.Create").subscribe(text => this.title = text);
  }

  private handleDataEdit(c: Category) {
    this.category = c
    this.fg.setValue({
      name: c.name ?? "",
      iconUrl: c.iconUrl ?? ""
    })
    this.translationService.get("category.change.Update").subscribe(text => this.title = text);
    this.reloading = false
  }

  cancel() {
    this.location.back()
  }

  submit() {
    if (!this.fg.valid) {
      this.translationService.get("category.message.error.formInvalid").subscribe(msg => this.toastService.error(msg))
      return
    }
    this.reloading = true

    if (this.category) {
      this.update()
    } else {
      this.create()
    }
  }

  private create() {
    let request = this.createRequest()
    if (!request) return
    this.service.createCategory(request).subscribe({
      next: category => {
        this.translationService.get("category.message.create.succeed").subscribe(
          msg => {
            this.toastService.success(msg)
            this.router.navigate(["/category"]).then()
          }
        )
      },
      error: err => this.translationService.get("category.message.create.failed").subscribe(
        msg => this.toastService.error(msg)
      )
    })
  }
  private update() {
    if (!this.category) return
    let request = this.createRequest()
    if (!request) return
    this.service.updateCategory(this.category.id, request).subscribe({
      next: category => {
        this.translationService.get("category.message.update.succeed").subscribe(
          msg => {
            this.toastService.success(msg)
            this.router.navigate(["/category"]).then()
          }
        )
      },
      error: err => this.translationService.get("category.message.update.failed").subscribe(
        msg => this.toastService.error(msg)
      )
    })
  }

  private createRequest(): CategoryChangeRequest | undefined {
    let value = this.fg.value
    return new CategoryChangeRequest(
      value.name,
      value.iconUrl
    )
  }
}

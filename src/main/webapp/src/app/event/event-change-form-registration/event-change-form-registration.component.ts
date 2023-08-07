import {Component, Input} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {ChipSelectEntry} from "../../shared/chip-select-pane/chip-select-entry";
import {CategoryService} from "../../category/model/category.service";
import {Category} from "../../category/model/category-api";
import {Page} from "../../shared/model/page";

@Component({
  selector: 'app-event-change-form-registration',
  templateUrl: './event-change-form-registration.component.html',
  styleUrls: ['./event-change-form-registration.component.scss']
})
export class EventChangeFormRegistrationComponent {
  @Input()
  set form(value: FormGroup) {
    this.fg = value
    this.categoryForm = value.get('categories') as FormControl
  }

  fg: FormGroup | undefined
  @Input() hiddenFields: string[] = []


  @Input() categoryForm: FormControl | undefined
  allCategories: ChipSelectEntry[] = []

  categoriesLoading: boolean = false

  constructor(private categoryService: CategoryService) {
  }

  ngOnInit() {
    this.categoriesLoading = true
    this.categoryService.getAllCategories(0, 100).subscribe(d => this.handleCategoryData(d))
  }


  isVisible(ctrl: string): boolean {
    return this.hiddenFields.find(x => x == ctrl) == null
  }

  get ticketsEnabled(): FormControl<any> {
    // @ts-ignore
    return this.form!!.get('ticketsEnabled')
  }

  private handleCategoryData(d: Page<Category>) {
    this.allCategories = d.content.map(d => ({id: d.id, name: d.name} as ChipSelectEntry))
    this.categoriesLoading = false
  }
}

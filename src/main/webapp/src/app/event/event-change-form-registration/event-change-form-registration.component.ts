import {Component, Input} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {ChipSelectEntry} from "../../shared/chip-select-pane/chip-select-entry";
import {CategoryService} from "../../category/model/category.service";
import {Category} from "../../category/model/category-api";
import {Page} from "../../shared/model/page";
import {MatChipInputEvent} from "@angular/material/chips";

@Component({
    selector: 'app-event-change-form-registration',
    templateUrl: './event-change-form-registration.component.html',
    styleUrls: ['./event-change-form-registration.component.scss'],
    standalone: false
})
export class EventChangeFormRegistrationComponent {
  fg: FormGroup | undefined
  @Input() hiddenFields: string[] = []
  @Input() categoryForm: FormControl | undefined
  allCategories: ChipSelectEntry[] = []
  categoriesLoading: boolean = false

  constructor(private categoryService: CategoryService) {
  }

  @Input()
  set form(value: FormGroup) {
    this.fg = value
    this.categoryForm = value.get('categories') as FormControl
  }

  get ticketsEnabled(): FormControl<any> {
    // @ts-ignore
    return this.form!!.get('ticketsEnabled')
  }

  ngOnInit() {
    this.categoriesLoading = true
    this.categoryService.getAllCategories(0, 100).subscribe(d => this.handleCategoryData(d))
  }

  isVisible(ctrl: string): boolean {
    return this.hiddenFields.find(x => x == ctrl) == null
  }

  private handleCategoryData(d: Page<Category>) {
    this.allCategories = d.content.map(d => ({id: d.id, name: d.name} as ChipSelectEntry))
    this.categoriesLoading = false
  }

  addTag(event: MatChipInputEvent) {
    const value = (event.value || '').trim()
    if (value.length <= 0) return
    const t = this.tags
    if (value && t) {
      let data = t.value as string[]
      let index = data.indexOf(value)
      if (index < 0) data.push(value)
    }
    event.chipInput!.clear();
  }

  get tags(): FormControl {
    return this.fg?.get('tags') as FormControl;
  }

  removeTag(tag: string) {
    let t = this.tags
    if (!t) return

    let index = (t.value as string[]).indexOf(tag)
    if (index >= 0) {
      (t.value as string[]).splice(index, 1)
    }
  }
}

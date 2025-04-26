import {Component, effect, input, output} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatFormField, MatHint, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {TranslatePipe} from "@ngx-translate/core";
import {Category, CategoryChangeRequest} from '../category-api';

@Component({
  selector: 'lib-category-change',
  imports: [
    FormsModule,
    MatFormField,
    MatHint,
    MatInput,
    MatLabel,
    ReactiveFormsModule,
    TranslatePipe
  ],
  templateUrl: './category-change.component.html',
  styleUrl: './category-change.component.scss'
})
export class CategoryChangeComponent {

  data = input<Category>()
  request = output<CategoryChangeRequest>()

  fg: FormGroup

  constructor(private fb: FormBuilder) {
    this.fg = this.fb.group({
      name: this.fb.control('', Validators.required),
      iconUrl: this.fb.control(''),
    })


    effect(() => {
      let category = this.data()
      if(category) this.handleDataChanged(category)
    });
  }



  private handleDataChanged(category: Category) {
    this.fg.get('name')?.setValue(category.name)
    this.fg.get('iconUrl')?.setValue(category.iconUrl)
  }

  submit() {
    if (!this.fg.valid) return
    let value = this.fg.value
    let request = new CategoryChangeRequest(value.name, value.iconUrl)
    this.request.emit(request)
  }
}

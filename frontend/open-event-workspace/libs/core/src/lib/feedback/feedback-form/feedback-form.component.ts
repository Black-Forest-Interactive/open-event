import {Component, OnInit, output} from '@angular/core';
import {MatFormFieldModule} from "@angular/material/form-field";
import {FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatSliderModule} from "@angular/material/slider";
import {MatButtonModule} from "@angular/material/button";
import {MatChipInputEvent, MatChipsModule} from "@angular/material/chips";
import {MatSelectModule} from "@angular/material/select";
import {MatIconModule} from "@angular/material/icon";
import {FeedbackChangeRequest} from "@open-event-workspace/core";
import {MatInput} from "@angular/material/input";
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'lib-feedback-form',
  imports: [
    MatFormFieldModule,
    MatSliderModule,
    MatButtonModule,
    MatChipsModule,
    MatSelectModule,
    MatIconModule,
    ReactiveFormsModule,
    MatInput,
    TranslatePipe,
  ],
  templateUrl: './feedback-form.component.html',
  styleUrl: './feedback-form.component.scss'
})
export class FeedbackFormComponent implements OnInit {
  feedbackForm: FormGroup
  separatorKeysCodes: number[] = [13, 188]

  request = output<FeedbackChangeRequest>()

  constructor(private fb: FormBuilder) {
    this.feedbackForm = this.fb.group({
      // subject: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required, Validators.minLength(10)]],
      topic: ['', Validators.required],
      // tags: this.fb.array([]),
      rating: [3, [Validators.min(1), Validators.max(5)]]
    })
  }

  ngOnInit(): void {
  }

  get tagsArray(): FormArray {
    return this.feedbackForm.get('tags') as FormArray
  }

  addTag(event: MatChipInputEvent): void {
    const value = (event.value || '').trim()
    if (value) {
      this.tagsArray.push(this.fb.control(value))
    }
    event.chipInput!.clear()
  }

  removeTag(index: number): void {
    if (index >= 0) {
      this.tagsArray.removeAt(index)
    }
  }

  onSubmit(): void {
    if (this.feedbackForm.valid) {
      const formValue = this.feedbackForm.value
      const feedbackRequest = new FeedbackChangeRequest(
        // formValue.subject,
        '',
        formValue.description,
        formValue.topic,
        // formValue.tags,
        [],
        formValue.rating
      )
      this.request.emit(feedbackRequest)
      this.onReset()
    }
  }

  onReset(): void {
    this.feedbackForm.reset()
    this.tagsArray.clear()
    this.feedbackForm.patchValue({rating: 3})
  }
}

import { Component, inject, output } from '@angular/core'
import { MatFormFieldModule } from '@angular/material/form-field'
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms'
import { MatSliderModule } from '@angular/material/slider'
import { MatButtonModule } from '@angular/material/button'
import { MatSelectModule } from '@angular/material/select'
import { MatInput } from '@angular/material/input'
import { TranslatePipe } from '@ngx-translate/core'
import { FeedbackChangeRequest } from '../feedback.api'

@Component({
  selector: 'lib-feedback-form',
  imports: [MatFormFieldModule, MatSliderModule, MatButtonModule, MatSelectModule, ReactiveFormsModule, MatInput, TranslatePipe],
  templateUrl: './feedback-form.component.html',
  styleUrl: './feedback-form.component.scss'
})
export class FeedbackFormComponent {
  private fb = inject(FormBuilder)

  feedbackForm: FormGroup = this.fb.group({
    description: ['', [Validators.required, Validators.minLength(10)]],
    topic: ['', Validators.required],
    rating: [3, [Validators.min(1), Validators.max(5)]]
  })

  request = output<FeedbackChangeRequest>()

  onSubmit(): void {
    if (this.feedbackForm.valid) {
      const v = this.feedbackForm.value
      this.request.emit(new FeedbackChangeRequest('', v.description, v.topic, [], v.rating))
      this.onReset()
    }
  }

  onReset(): void {
    this.feedbackForm.reset()
    this.feedbackForm.patchValue({ rating: 3 })
  }
}

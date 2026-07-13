import { Component, input, ChangeDetectionStrategy } from '@angular/core'
import { Feedback } from '@open-event/core'
import { DatePipe } from '@angular/common'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  selector: 'admin-feedback-card',
  imports: [DatePipe, TranslatePipe],
  templateUrl: './feedback-card.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './feedback-card.component.scss'
})
export class FeedbackCardComponent {
  feedback = input.required<Feedback>()
}

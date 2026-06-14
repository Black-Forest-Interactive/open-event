import { Component, inject } from '@angular/core'
import { Feedback } from '@open-event/core'
import { MAT_DIALOG_DATA, MatDialogContent } from '@angular/material/dialog'
import { TranslatePipe } from '@ngx-translate/core'
import { FeedbackCardComponent } from '../feedback-card/feedback-card.component'

@Component({
  selector: 'admin-feedback-details-dialog',
  imports: [MatDialogContent, TranslatePipe, FeedbackCardComponent],
  templateUrl: './feedback-details-dialog.component.html',
  styleUrl: './feedback-details-dialog.component.scss'
})
export class FeedbackDetailsDialogComponent {
  feedback: Feedback = inject(MAT_DIALOG_DATA)
}

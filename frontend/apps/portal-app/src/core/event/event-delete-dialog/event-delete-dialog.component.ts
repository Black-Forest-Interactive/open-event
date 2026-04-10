import { Component, inject } from '@angular/core'
import { MAT_DIALOG_DATA, MatDialogActions, MatDialogClose, MatDialogContent, MatDialogTitle } from '@angular/material/dialog'
import { Event } from '@open-event/core'
import { TranslatePipe } from '@ngx-translate/core'
import { MatButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'

@Component({
  selector: 'portal-event-delete-dialog',
  templateUrl: './event-delete-dialog.component.html',
  styleUrls: ['./event-delete-dialog.component.scss'],
  imports: [MatDialogTitle, MatDialogContent, TranslatePipe, MatDialogActions, MatButton, MatIcon, MatDialogClose],
  standalone: true
})
export class EventDeleteDialogComponent {
  data = inject<Event>(MAT_DIALOG_DATA)
}

import { Component, signal } from '@angular/core'
import { MatDialogActions, MatDialogContent, MatDialogTitle } from '@angular/material/dialog'
import { AccountComponent } from '../../account/account/account.component'
import { AccountDisplayNamePipe, EventSearchEntry } from '@open-event/core'
import { DatePipe } from '@angular/common'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'
import { MatButton } from '@angular/material/button'
import { Subject } from 'rxjs'

@Component({
  selector: 'portal-event-board-map-popup',
  templateUrl: './event-board-map-popup.component.html',
  styleUrls: ['./event-board-map-popup.component.scss'],
  imports: [MatDialogContent, AccountComponent, MatDialogTitle, AccountDisplayNamePipe, DatePipe, MatDialogActions, MatIcon, TranslatePipe, MatButton],
  standalone: true
})
export class EventBoardMapPopupComponent {
  data = signal<EventSearchEntry | undefined>(undefined)
  close = new Subject<boolean>()

  onDetailsClick() {
    this.close.next(true)
  }

  onCloseClick() {
    this.close.next(false)
  }
}

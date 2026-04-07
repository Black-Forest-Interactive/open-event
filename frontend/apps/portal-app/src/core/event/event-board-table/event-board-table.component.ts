import { Component, inject } from '@angular/core'
import { EventBoardService } from '../event-board.service'
import { DatePipe } from '@angular/common'
import { MatCard } from '@angular/material/card'
import { MatTableModule } from '@angular/material/table'
import { TranslatePipe } from '@ngx-translate/core'
import { AccountComponent } from '../../account/account/account.component'
import { MatDivider } from '@angular/material/divider'
import { RouterLink } from '@angular/router'
import { MatPaginator } from '@angular/material/paginator'
import { LoadingBarComponent } from '@open-event/shared'
import { RegistrationStatusComponent } from '@open-event/core'

@Component({
  selector: 'portal-event-board-table',
  templateUrl: './event-board-table.component.html',
  styleUrl: './event-board-table.component.scss',
  imports: [MatCard, TranslatePipe, AccountComponent, DatePipe, MatDivider, RouterLink, MatPaginator, MatTableModule, LoadingBarComponent, RegistrationStatusComponent],
  standalone: true
})
export class EventBoardTableComponent {
  protected service = inject(EventBoardService)
  readonly displayedColumns = ['title', 'period', 'location', 'account', 'status']
}

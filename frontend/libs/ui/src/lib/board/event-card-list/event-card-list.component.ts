import { Component, input, output } from '@angular/core'
import { MatFabButton, MatButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'
import { LoadingBarComponent, ScrollNearEndDirective } from '@open-event/shared'

@Component({
  selector: 'lib-event-card-list',
  templateUrl: './event-card-list.component.html',
  imports: [MatFabButton, MatButton, MatIcon, TranslatePipe, LoadingBarComponent, ScrollNearEndDirective]
})
export class EventCardListComponent {
  reloading = input.required<boolean>()
  hasMore = input.required<boolean>()

  loadMore = output()
  filterClicked = output()
}

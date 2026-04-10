import { Component, Directive, input } from '@angular/core'
import { LoadingBarComponent } from '@open-event/shared'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  imports: [LoadingBarComponent, TranslatePipe],
  selector: 'admin-board-card',
  styleUrl: './board-card.component.scss',
  templateUrl: './board-card.component.html'
})
export class BoardCardComponent {
  reloading = input(false)
  title = input.required<string>()
}

@Directive({
  selector: `board-card-toolbar-actions, [board-card-toolbar-actions], [boardCardToolbarActions]`
})
export class BoardCardToolbarActions {}

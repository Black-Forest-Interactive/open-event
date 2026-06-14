import { Component, Directive, input, output } from '@angular/core'
import { LoadingBarComponent } from '@open-event/shared'
import { MatIconButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { BoardSearchComponent } from '@open-event/ui'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  selector: 'admin-board',
  imports: [LoadingBarComponent, MatIconButton, MatIcon, BoardSearchComponent, TranslatePipe],
  templateUrl: './board.component.html',
  styleUrl: './board.component.scss'
})
export class BoardComponent {
  reloading = input(false)
  title = input('')
  searchPlaceholder = input('')
  showBack = input(false)
  showReload = input(true)
  showSearch = input(true)

  search = output<string>()
  reload = output<boolean>()
  back = output<boolean>()
}

@Directive({ selector: 'board-toolbar-actions, [board-toolbar-actions]' })
export class BoardToolbarActions {}

@Directive({ selector: 'board-filters, [board-filters]' })
export class BoardFilters {}

import { Component, Directive, input, output } from '@angular/core'
import { LoadingBarComponent } from '@open-event/shared'
import { MatIconButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { BoardToolbarSearchComponent } from '../board-toolbar-search/board-toolbar-search.component'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  selector: 'admin-board',
  imports: [LoadingBarComponent, MatIconButton, MatIcon, BoardToolbarSearchComponent, TranslatePipe],
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

  fullTextSearch = output<string>()
  reload = output<boolean>()
  back = output<boolean>()
}

@Directive({
  selector: `board-toolbar-actions, [board-toolbar-actions], [boardToolbarActions]`
})
export class BoardToolbarActions {}

@Directive({
  selector: `board-filters, [board-filters], [boardFilters]`
})
export class BoardFilters {}

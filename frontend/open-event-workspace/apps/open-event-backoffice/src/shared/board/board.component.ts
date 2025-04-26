import {Component, Directive, input, output} from '@angular/core';
import {LoadingBarComponent} from "@open-event-workspace/shared";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {BoardToolbarSearchComponent} from "../board-toolbar-search/board-toolbar-search.component";
import {MatToolbar} from "@angular/material/toolbar";
import {TranslatePipe} from "@ngx-translate/core";
import {MatCard} from "@angular/material/card";

@Component({
  selector: 'app-board',
  imports: [
    LoadingBarComponent,
    MatButtonModule,
    MatIconModule,
    BoardToolbarSearchComponent,
    MatToolbar,
    TranslatePipe,
    MatCard
  ],
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
  selector: `board-toolbar-actions, [board-toolbar-actions], [boardToolbarActions]`,
})
export class BoardToolbarActions {
}

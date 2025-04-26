import {Component, Directive, input} from '@angular/core';
import {LoadingBarComponent} from "@open-event-workspace/shared";
import {MatToolbar} from "@angular/material/toolbar";
import {TranslatePipe} from "@ngx-translate/core";
import {MatCard} from "@angular/material/card";

@Component({
  imports: [
    LoadingBarComponent,
    MatToolbar,
    TranslatePipe,
    MatCard
  ],
  selector: 'app-board-card',
  styleUrl: './board-card.component.scss',
  templateUrl: './board-card.component.html'
})
export class BoardCardComponent {
  reloading = input(false)
  title = input.required<string>()
}

@Directive({
  selector: `board-card-toolbar-actions, [board-card-toolbar-actions], [boardCardToolbarActions]`,
})
export class BoardCardToolbarActions {

}

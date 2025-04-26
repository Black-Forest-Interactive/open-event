import {Component, input} from '@angular/core';
import {Account} from "@open-event-workspace/core";
import {TranslatePipe} from "@ngx-translate/core";
import {DatePipe} from "@angular/common";
import {BoardCardComponent} from "../../../shared/board-card/board-card.component";

@Component({
  selector: 'app-account-details-title',
  imports: [
    TranslatePipe,
    DatePipe,
    BoardCardComponent
  ],
  templateUrl: './account-details-title.component.html',
  styleUrl: './account-details-title.component.scss'
})
export class AccountDetailsTitleComponent {
  data = input.required<Account>()
}

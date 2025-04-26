import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TranslatePipe} from "@ngx-translate/core";
import {MatTabLink, MatTabNav} from "@angular/material/tabs";
import {RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";
import {MatToolbar} from "@angular/material/toolbar";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-account-board',
  imports: [CommonModule, MatToolbar, TranslatePipe, MatTabLink, RouterLink, RouterLinkActive, MatTabNav, MatIcon, RouterOutlet],
  templateUrl: './account-board.component.html',
  styleUrl: './account-board.component.scss',
})
export class AccountBoardComponent {
}

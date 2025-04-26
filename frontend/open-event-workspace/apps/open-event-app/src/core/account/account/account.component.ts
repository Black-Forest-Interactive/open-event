import {Component, input} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AccountDisplayNamePipe, AccountInfo} from "@open-event-workspace/core";
import {FALLBACK, GravatarModule, RATING} from "ngx-gravatar";

@Component({
  selector: 'app-account',
  imports: [CommonModule, AccountDisplayNamePipe, GravatarModule],
  templateUrl: './account.component.html',
  styleUrl: './account.component.scss',
})
export class AccountComponent {

  account = input.required<AccountInfo>()
  showUserName = input(true)

  protected readonly FALLBACK = FALLBACK;
  protected readonly RATING = RATING;
}

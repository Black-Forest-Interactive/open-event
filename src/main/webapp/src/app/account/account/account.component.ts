import {Component, Input} from '@angular/core';
import {Account} from "../model/account-api";

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss']
})
export class AccountComponent {
  @Input() account: Account | undefined
  @Input() showUserName: boolean = true


}

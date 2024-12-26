import {Component, Input} from '@angular/core';
import {AccountInfo} from "../model/account-api";

@Component({
    selector: 'app-account',
    templateUrl: './account.component.html',
    styleUrls: ['./account.component.scss'],
    standalone: false
})
export class AccountComponent {
  @Input() account: AccountInfo | undefined
  @Input() showUserName: boolean = true


}
